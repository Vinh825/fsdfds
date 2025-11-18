package com.healthcare.patient.dto.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateTestOrderItemRequest {

    /** BẮT BUỘC vì DB đang NOT NULL */
    @NotNull
    private Long testTypeId; // đổi sang UUID nếu DB là uuid

    @NotBlank
    private String testName;

    private String testCode;
    private String priority;     // optional
    private String specimenType; // optional
    private String notes;        // optional
}
