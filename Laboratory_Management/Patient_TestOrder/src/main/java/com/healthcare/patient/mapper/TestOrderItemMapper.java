package com.healthcare.patient.mapper;

import com.healthcare.patient.dto.item.*;
import com.healthcare.patient.entity.TestOrderItem;

public final class TestOrderItemMapper {
    private TestOrderItemMapper(){}

    public static TestOrderItemResponse toResponse(TestOrderItem e) {
        return TestOrderItemResponse.builder()
                .id(e.getId())
                .testTypeId(e.getTestTypeId())
                .testName(e.getTestName())
                .testCode(e.getTestCode())
                .priority(e.getPriority())
                .status(e.getStatus())
                .specimenType(e.getSpecimenType())
                .notes(e.getNotes())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    public static void applyUpdate(TestOrderItem e, UpdateTestOrderItemRequest dto) {
        if (dto.getTestTypeId() != null) e.setTestTypeId(dto.getTestTypeId());
        if (dto.getTestName() != null) e.setTestName(dto.getTestName());
        if (dto.getTestCode() != null) e.setTestCode(dto.getTestCode());
        if (dto.getPriority() != null) e.setPriority(dto.getPriority());
        if (dto.getSpecimenType() != null) e.setSpecimenType(dto.getSpecimenType());
        if (dto.getStatus() != null) e.setStatus(dto.getStatus());
        if (dto.getNotes() != null) e.setNotes(dto.getNotes());
    }
}
