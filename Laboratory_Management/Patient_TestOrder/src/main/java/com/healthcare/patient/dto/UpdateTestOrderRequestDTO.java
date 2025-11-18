package com.healthcare.patient.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateTestOrderRequestDTO {
    @NotBlank private String patientName;
    @NotNull @Min(0) @Max(150) private Integer age;
    @NotBlank private String gender;
    @NotBlank private String address;
    @NotBlank private String phoneNumber;
    @Email @NotBlank private String email;
    @NotBlank private String updatedBy;
}
