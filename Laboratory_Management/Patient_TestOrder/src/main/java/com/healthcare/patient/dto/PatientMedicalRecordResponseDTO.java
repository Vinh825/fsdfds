package com.healthcare.patient.dto;

import com.healthcare.patient.entity.Patient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientMedicalRecordResponseDTO {

    private Long patientMedicalRecordId;
    private Long patientId;
    private String patientCode;
    private String recordType;
    private String medicalHistory;
    private String currentMedications;
    private String allergies;
    private String clinicalNotes;
    private LocalDate lastTestDate;
    private Integer patientAgeAtRecord;
    private Boolean isCurrent;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}

