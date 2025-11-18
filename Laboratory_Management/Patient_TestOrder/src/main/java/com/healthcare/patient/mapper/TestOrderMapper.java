package com.healthcare.patient.mapper;

import com.healthcare.patient.entity.Patient;
import com.healthcare.patient.dto.CreateTestOrderRequest;
import com.healthcare.patient.dto.TestOrderResponseDTO;
import com.healthcare.patient.entity.PatientSnapshot;
import com.healthcare.patient.entity.TestOrder;

import java.time.LocalDate;
import java.time.Period;

public final class TestOrderMapper {
    private TestOrderMapper() {
    }

    /* Snapshot từ Patient để TestOrder không phụ thuộc schema Patient về sau */
    public static PatientSnapshot toSnapshot(Patient p) {
        if (p == null)
            return null;
        return PatientSnapshot.builder()
                .patientCode(p.getPatientCode())
                .fullName(p.getFullName())
                .dateOfBirth(p.getDateOfBirth())
                .gender(p.getGender())
                .address(p.getAddress())
                .phoneNumber(p.getPhoneNumber())
                .email(p.getEmail())
                .identityNumber(p.getIdentityNumber())
                .build();
    }

    /* Tính tuổi tại thời điểm tạo order */
    public static Integer calcAge(LocalDate dob) {
        if (dob == null)
            return null;
        return Period.between(dob, LocalDate.now()).getYears();
    }

    /* Build TestOrder từ CreateTestOrderRequest + Patient (ID kiểu Long) */
    public static TestOrder toEntity(CreateTestOrderRequest req, Patient p, String orderNumber, String createdBy) {
        var snapshot = toSnapshot(p);
        return TestOrder.builder()
                .patientId(p.getPatientId()) // Long
                .patient(snapshot) // @Embedded snapshot
                .age(calcAge(p.getDateOfBirth()))
                .status("PENDING")
                .createdBy(createdBy)
                .orderNumber(orderNumber) // đã generate ở service
                .isDeleted(false)
                .build();
    }

    /* Map entity → DTO trả về API (id là Long) */
    public static TestOrderResponseDTO toResponse(TestOrder e) {
        var snap = e.getPatient();
        return TestOrderResponseDTO.builder()
                .id(e.getId()) // Long
                .patientName(snap != null ? snap.getFullName() : null)
                .age(e.getAge())
                .gender(snap != null ? snap.getGender() : null)
                .phoneNumber(snap != null ? snap.getPhoneNumber() : null)
                .status(e.getStatus())
                .createdAt(e.getCreatedAt())
                .createdBy(e.getCreatedBy())
                .runDate(e.getRunDate())
                .runBy(e.getRunBy())
                .build();
    }
}
