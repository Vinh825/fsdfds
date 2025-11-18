package com.healthcare.patient.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TestOrderResponseDTO {
    private Long id; // ✅ BIGINT
    private String patientName;
    private Integer age;
    private String gender;
    private String phoneNumber;
    private String status;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime runDate;
    private String runBy;
}
