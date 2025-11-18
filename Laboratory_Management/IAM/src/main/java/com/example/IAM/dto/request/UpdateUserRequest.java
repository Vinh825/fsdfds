package com.example.IAM.dto.request;

import com.example.IAM.entity.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserRequest {
    @Email
    private String email;

    private String firstName;

    private String lastName;


    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Pattern(regexp = "^(0|\\+84)(3[2-9]|5[25689]|7[06-9]|8[1-689]|9[0-46-9])\\d{7}$",
            message = "Invalid mobile phone number")
    private String phoneNumber;

    private String address;

    private LocalDate dateOfBirth;
}
