package com.example.IAM.dto.request;

import com.example.IAM.entity.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class CreateUserRequest {

    @Email
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private Gender gender;

    @Pattern(regexp = "^(0|\\+84)(3[2-9]|5[25689]|7[06-9]|8[1-689]|9[0-46-9])\\d{7}$", message = "Invalid mobile phone number")
    private String phoneNumber;

    private String address;

    private LocalDate dateOfBirth;

    private Set<String> roleCodes;
}
