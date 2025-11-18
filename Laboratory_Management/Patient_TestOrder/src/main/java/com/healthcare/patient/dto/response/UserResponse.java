package com.healthcare.patient.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class UserResponse {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    String identityNumber;
    private String phoneNumber;
    private String address;
    private String gender;
    private Integer age;
    private LocalDate dateOfBirth;
}
