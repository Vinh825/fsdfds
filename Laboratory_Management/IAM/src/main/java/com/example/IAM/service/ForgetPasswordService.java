package com.example.IAM.service;

import com.example.IAM.common.exception.AppException;
import com.example.IAM.common.BuildOtpEmail;
import com.example.IAM.common.exception.ErrorCode;
import com.example.IAM.dto.request.MailRequest.EmailRequest;
import com.example.IAM.dto.request.MailRequest.SendEmailRequest;
import com.example.IAM.dto.request.MailRequest.Sender;
import com.example.IAM.dto.request.ResetPasswordDto;
import com.example.IAM.dto.request.VerifyOtpDto;
import com.example.IAM.dto.respone.EmailResponse;
import com.example.IAM.entity.ForgetPassword;
import com.example.IAM.entity.PasswordState;
import com.example.IAM.entity.User;
import com.example.IAM.repository.ForgotOtpRepository;
import com.example.IAM.repository.UserRepository;
import com.example.IAM.utils.EmailClient;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.List;

import static com.example.IAM.service.UserService.PASSWORD_MAX_AGE_DAYS;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ForgetPasswordService {

    EmailClient emailClient;
    ForgotOtpRepository forgotOtpRepository;
    UserRepository userRepository;

    final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    final String apiKey;

    public ForgetPasswordService(
            EmailClient emailClient,
            ForgotOtpRepository forgotOtpRepository,
            UserRepository userRepository,
            @Value("${brevo.api.key}") String apiKey) {
        this.emailClient = emailClient;
        this.forgotOtpRepository = forgotOtpRepository;
        this.userRepository = userRepository;
        this.apiKey = apiKey;
    }

    static final long OTP_TTL_SECONDS = 300;
    static final int MAX_ATTEMPTS = 5;

    public EmailResponse sendEmail(SendEmailRequest request) {
        String email = request.getTo().getEmail();
        String code = generateOtp6Digits();
        String codeHash = passwordEncoder.encode(code);
        String username = request.getTo().getUsername();

        if (username == null || username.isBlank()) {
            throw new AppException(ErrorCode.USERNAME_NOT_FOUND);
        }

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!username.equals(user.getUsername())) {
            throw new AppException(ErrorCode.USERNAME_NOT_FOUND);
        }
        if (Boolean.TRUE.equals(user.getIsLocked()))
            throw new AppException(ErrorCode.USER_LOCKED);
        if (!Boolean.TRUE.equals(user.getIsActive()))
            throw new AppException(ErrorCode.USER_INACTIVE);
        if (user.getDeleted()) throw new AppException(ErrorCode.USER_DELETED);
        forgotOtpRepository.save(ForgetPassword.builder()
                .email(email)
                .used(false)
                .attempts(0)
                .codeHash(codeHash)
                .createdAt(OffsetDateTime.now())
                .ttlSeconds(OTP_TTL_SECONDS)
                .build());

        request.setSubject("Password reset authentication code");
        request.setHtmlContent(BuildOtpEmail.buildOtpEmailHtml(code));

        EmailRequest emailRequest = EmailRequest.builder()
                .sender(Sender.builder().name("Laboratory Management").email("iphone0868369069@gmail.com").build())
                .to(List.of(request.getTo()))
                .htmlContent(request.getHtmlContent())
                .subject(request.getSubject())
                .build();
        try {
            return emailClient.sendEmail(apiKey, emailRequest);
        } catch (FeignException e) {
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }

    public void verifyOtp(VerifyOtpDto req) {
        var record = forgotOtpRepository.findById(req.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.OTP_NOT_FOUND));

        if (Boolean.TRUE.equals(record.getUsed())) {
            throw new AppException(ErrorCode.OTP_USED);
        }

        boolean ok = passwordEncoder.matches(req.getCode(), record.getCodeHash());
        if (!ok) {
            int attempts = record.getAttempts() == null ? 0 : record.getAttempts();
            attempts++;
            record.setAttempts(attempts);

            forgotOtpRepository.save(record);

            if (attempts >= MAX_ATTEMPTS) {
                forgotOtpRepository.deleteById(req.getEmail());
            }
            throw new AppException(ErrorCode.OTP_INCORRECT);
        }
    }

    public void resetPassword(ResetPasswordDto req) {
        if (!req.getNewPassword().equals(req.getConfirmNewPassword()))
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (Boolean.TRUE.equals(user.getIsLocked()))
            throw new AppException(ErrorCode.USER_LOCKED);
        if (!Boolean.TRUE.equals(user.getIsActive()))
            throw new AppException(ErrorCode.USER_INACTIVE);
        if (user.getDeleted()) throw new AppException(ErrorCode.USER_DELETED);

        var record = forgotOtpRepository.findById(req.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.OTP_NOT_FOUND));

        if (Boolean.TRUE.equals(record.getUsed()))
            throw new AppException(ErrorCode.OTP_USED);

        if (!passwordEncoder.matches(req.getCode(), record.getCodeHash())) {
            int attempts = record.getAttempts() == null ? 0 : record.getAttempts();
            attempts++;
            record.setAttempts(attempts);
            forgotOtpRepository.save(record);
            if (attempts >= MAX_ATTEMPTS) {
                forgotOtpRepository.deleteById(req.getEmail());
            }
            throw new AppException(ErrorCode.OTP_INCORRECT);
        }

        user.setPassword(passwordEncoder.encode(req.getNewPassword()));

        OffsetDateTime now = OffsetDateTime.now();
        PasswordState state = user.getPasswordState();
        if (state == null) {
            state = PasswordState.builder()
                    .user(user)
                    .passwordChangedAt(now)
                    .passwordExpiryDate(now.plusDays(PASSWORD_MAX_AGE_DAYS))
                    .build();
        } else {
            state.setPasswordChangedAt(now);
            state.setPasswordExpiryDate(now.plusDays(PASSWORD_MAX_AGE_DAYS));
        }
        user.setPasswordState(state);

        userRepository.save(user);

        record.setUsed(true);
        record.setUsedAt(now);
        forgotOtpRepository.save(record);
    }

    private String generateOtp6Digits() {
        SecureRandom rnd = new SecureRandom();
        int v = rnd.nextInt(1_000_000);
        return String.format("%06d", v);
    }

}
