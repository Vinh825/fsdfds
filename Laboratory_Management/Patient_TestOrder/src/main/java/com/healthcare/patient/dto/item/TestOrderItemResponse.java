package com.healthcare.patient.dto.item;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TestOrderItemResponse {
    private Long id; // ✅ BIGINT
    private Long testTypeId;
    private String testName;
    private String testCode;
    private String priority;
    private String status;
    private String specimenType;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
