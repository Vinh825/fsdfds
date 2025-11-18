
package com.example.IAM.service;

import com.example.IAM.common.SendNewUserCreate;
import com.example.IAM.common.exception.AppException;
import com.example.IAM.common.exception.ErrorCode;
import com.example.IAM.dto.request.MailRequest.EmailRequest;
import com.example.IAM.dto.request.MailRequest.Recipient;
import com.example.IAM.dto.request.MailRequest.Sender;
import com.example.IAM.utils.EmailClient;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailCreateAccount {

    EmailClient emailClient;
    String apiKey;

    private static final String SENDER_EMAIL = "iphone0868369069@gmail.com";
    private static final String SENDER_NAME = "Healthcare Laboratory";


    public MailCreateAccount(EmailClient emailClient,
                             @Value("${brevo.api.key}") String apiKey) {
        this.emailClient = emailClient;
        this.apiKey = apiKey;
    }

    public void sendEmailCreateAccount(String toEmail, String toName, String username, String password) {
        String content = SendNewUserCreate.sendNewUserCreate(username, password);

        Sender sender = Sender.builder()
                .email(SENDER_EMAIL)
                .name(SENDER_NAME)
                .build();

        Recipient recipient = Recipient.builder()
                .email(toEmail)
                .name(toName)
                .build();

        EmailRequest emailRequest = EmailRequest.builder()
                .sender(sender)
                .htmlContent(content)
                .to(List.of(recipient))
                .subject("Welcome! Your account information")
                .build();

        try {
            emailClient.sendEmail(apiKey, emailRequest);
        } catch (FeignException e) {
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }
}