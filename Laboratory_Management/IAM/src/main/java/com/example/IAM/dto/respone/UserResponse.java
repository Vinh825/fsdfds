package com.example.IAM.dto.respone;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
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
    private List<String> roleCodes;
    private Boolean locked;
    private Boolean active;
}
