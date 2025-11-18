package com.healthcare.patient.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientRequestDTO {


    private String patientCode;

    @NotBlank(message = "Full name is required")
    private String fullName;

    private LocalDate dateOfBirth;

    private String gender;

    private String address;

    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must be 10-15 digits")
    private String phoneNumber;

    @Email(message = "Email should be valid")
    private String email;

    private String identityNumber;

    private String emergencyContactName;

    @Pattern(regexp = "^[0-9]{10,15}$", message = "Emergency contact phone must be 10-15 digits")
    private String emergencyContactPhone;

    private Boolean isActive = true;
}


