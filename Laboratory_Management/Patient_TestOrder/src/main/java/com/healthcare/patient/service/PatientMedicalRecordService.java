package com.healthcare.patient.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.patient.dto.PatientMedicalRecordRequestDTO;
import com.healthcare.patient.dto.PatientMedicalRecordResponseDTO;
import com.healthcare.patient.entity.Patient;
import com.healthcare.patient.entity.PatientMedicalRecord;
import com.healthcare.patient.repository.PatientMedicalRecordRepository;
import com.healthcare.patient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PatientMedicalRecordService {

    @Autowired
    private PatientMedicalRecordRepository recordRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PatientMedicalRecordResponseDTO createRecord(PatientMedicalRecordRequestDTO requestDTO, String createdBy) {
        Patient patient = patientRepository.findById(requestDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + requestDTO.getPatientId()));
        Patient snapshotPatient;
        try {
            // Sử dụng ObjectMapper để tạo bản sao (deep clone)
            // và cắt đứt các quan hệ (proxy) của Hibernate
             snapshotPatient = objectMapper.convertValue(patient, Patient.class);
        } catch (IllegalArgumentException e) {
            // Xử lý lỗi nếu việc clone không thành công (thường do cấu hình Jackson)
            throw new RuntimeException("Error Create Of snapshot for Patient Entity", e);
        }


        PatientMedicalRecord record = new PatientMedicalRecord();
        mapDTOToEntity(requestDTO, record);
        record.setPatient(patient);
        record.setCreatedBy(createdBy);


        PatientMedicalRecord savedRecord = recordRepository.save(record);

        return mapEntityToDTO(savedRecord);
    }

    public PatientMedicalRecordResponseDTO updateRecord(Long recordId, PatientMedicalRecordRequestDTO requestDTO, String updatedBy) {
        PatientMedicalRecord record = recordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Medical record not found with id: " + recordId));
        mapDTOToEntity(requestDTO, record);
        record.setUpdatedBy(updatedBy);
        PatientMedicalRecord updatedRecord = recordRepository.save(record);
        return mapEntityToDTO(updatedRecord);
    }

    public PatientMedicalRecordResponseDTO getRecordById(Long recordId) {
        PatientMedicalRecord record = recordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Medical record not found with id: " + recordId));
        return mapEntityToDTO(record);
    }

    public List<PatientMedicalRecordResponseDTO> getRecordsByPatientId(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        List<PatientMedicalRecord> records = recordRepository.findByPatientAndIsDeletedFalse(patient);
        return records.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    public List<PatientMedicalRecordResponseDTO> getCurrentRecordsByPatientId(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        List<PatientMedicalRecord> records = recordRepository.findByPatientAndIsCurrentTrueAndIsDeletedFalse(patient);
        return records.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    public void deleteRecord(Long recordId, String deletedBy) {
        PatientMedicalRecord record = recordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Medical record not found with id: " + recordId));

        record.setIsDeleted(true);
        record.setIsCurrent(false);
        record.setUpdatedBy(deletedBy);
        record.setUpdatedAt(LocalDateTime.now());

        recordRepository.save(record);
    }
    private void mapDTOToEntity(PatientMedicalRecordRequestDTO dto, PatientMedicalRecord entity) {
        entity.setRecordType(dto.getRecordType());
        entity.setMedicalHistory(dto.getMedicalHistory());
        entity.setCurrentMedications(dto.getCurrentMedications());
        entity.setAllergies(dto.getAllergies());
        entity.setClinicalNotes(dto.getClinicalNotes());
        entity.setLastTestDate(dto.getLastTestDate());
        entity.setPatientAgeAtRecord(dto.getPatientAgeAtRecord());

    }

    private PatientMedicalRecordResponseDTO mapEntityToDTO(PatientMedicalRecord entity) {
        PatientMedicalRecordResponseDTO dto = new PatientMedicalRecordResponseDTO();
        dto.setPatientMedicalRecordId(entity.getPatientMedicalRecordId());
        dto.setPatientId(entity.getPatient().getPatientId());
        dto.setPatientCode(entity.getPatient().getPatientCode());
        dto.setRecordType(entity.getRecordType());
        dto.setMedicalHistory(entity.getMedicalHistory());
        dto.setCurrentMedications(entity.getCurrentMedications());
        dto.setAllergies(entity.getAllergies());
        dto.setClinicalNotes(entity.getClinicalNotes());
        dto.setLastTestDate(entity.getLastTestDate());
        dto.setPatientAgeAtRecord(entity.getPatientAgeAtRecord());
        dto.setIsCurrent(entity.getIsCurrent());
        dto.setIsDeleted(entity.getIsDeleted());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());
        return dto;
    }
}


