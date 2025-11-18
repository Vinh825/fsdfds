package com.example.IAM.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    PASSWORD_SAME_AS_OLD(1009, "New password must be different from old password", HttpStatus.BAD_REQUEST),
    PASSSWORD_OLD_INCORRECT(1010, "Old password is incorrect", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_MATCH(1011, "Password and confirm password do not match", HttpStatus.BAD_REQUEST),
    PASSWORD_POLICY_VIOLATION(1012, "Password does not meet the policy requirements", HttpStatus.BAD_REQUEST),
    USER_LOCKED(1013, "User account is locked", HttpStatus.LOCKED),
    USER_INACTIVE(1014, "User account is inactive", HttpStatus.FORBIDDEN),
    USER_DELETED(1015, "User account has been deleted", HttpStatus.GONE),
    CANNOT_SEND_EMAIL(1008, "Cannot send email", HttpStatus.BAD_REQUEST),
    OTP_NOT_FOUND(1100, "OTP not found", HttpStatus.NOT_FOUND),
    OTP_EXPIRED(1101, "OTP has expired", HttpStatus.GONE),
    OTP_USED(1102, "OTP has already been used", HttpStatus.BAD_REQUEST),
    OTP_INCORRECT(1103, "OTP is incorrect", HttpStatus.BAD_REQUEST),
    ACCOUNT_LOCKED(1016, "Account is temporarily locked", HttpStatus.LOCKED),
    PHONE_NUMBER_NOT_FOUND(1017, "The provided phone number is not registered", HttpStatus.NOT_FOUND),
    EMAIL_EXISTED(1018, "Email already exists", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_EXISTED(1019, "Phone number already exists", HttpStatus.BAD_REQUEST),
    USERNAME_NOT_FOUND(1020, "Username not found", HttpStatus.NOT_FOUND),
    ROLE_NOT_EXISTED(1021, "Role not existed", HttpStatus.NOT_FOUND),
    ROLE_EXISTED(1022, "Role code already exists", HttpStatus.BAD_REQUEST),
    ROLE_DELETED(1023, "Role has been deleted", HttpStatus.GONE),
    ROLE_IN_USE(1024, "Role is assigned to active users", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
