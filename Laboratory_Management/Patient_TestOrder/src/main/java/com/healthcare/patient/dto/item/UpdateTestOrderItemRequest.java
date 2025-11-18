package com.healthcare.patient.dto.item;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateTestOrderItemRequest {
    /** Cho phép cập nhật loại test; nếu null thì giữ nguyên */
    private Long testTypeId; // đổi sang UUID nếu DB là uuid

    private String testName;
    private String testCode;
    private String priority;
    private String specimenType;
    private String status; // cho phép đổi trạng thái
    private String notes;
}
