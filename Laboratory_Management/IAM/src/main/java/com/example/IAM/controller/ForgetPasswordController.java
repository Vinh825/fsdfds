package com.example.IAM.controller;


import com.example.IAM.dto.request.MailRequest.SendEmailRequest;
import com.example.IAM.dto.request.ResetPasswordDto;
import com.example.IAM.dto.request.VerifyOtpDto;
import com.example.IAM.dto.respone.ApiResponse;
import com.example.IAM.dto.respone.EmailResponse;
import com.example.IAM.service.ForgetPasswordService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/forget")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ForgetPasswordController {
    ForgetPasswordService service;

    @PostMapping()
    ApiResponse<EmailResponse> sendEmail(@Valid @RequestBody SendEmailRequest request) {
        return ApiResponse.<EmailResponse>builder()
                .result(service.sendEmail(request))
                .build();
    }

    @PostMapping("/verify")
    public ApiResponse<Void> verify(@Valid @RequestBody VerifyOtpDto req) {
        service.verifyOtp(req);
        return ApiResponse.<Void>builder().message("OTP is valid").build();
    }

    @PostMapping("/reset")
    public ApiResponse<Void> reset(@Valid @RequestBody ResetPasswordDto req) {
        service.resetPassword(req);
        return ApiResponse.<Void>builder().message("Password updated").build();
    }
}
