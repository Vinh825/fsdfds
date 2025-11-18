package com.healthcare.patient.dto;

import com.healthcare.patient.entity.Patient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientMedicalRecordRequestDTO {

    private Long patientId;
    private String recordType;
    private String medicalHistory;
    private String currentMedications;
    private String allergies;
    private String clinicalNotes;
    private LocalDate lastTestDate;
    private Integer patientAgeAtRecord;
    private String changeReason;
}


