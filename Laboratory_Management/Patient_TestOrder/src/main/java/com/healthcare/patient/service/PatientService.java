package com.healthcare.patient.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.patient.dto.PatientRequestDTO;
import com.healthcare.patient.dto.PatientResponseDTO;
import com.healthcare.patient.entity.Patient;
import com.healthcare.patient.repository.PatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PatientResponseDTO createPatient(PatientRequestDTO requestDTO, String createdBy) {
        if (patientRepository.existsByPatientCode(requestDTO.getPatientCode())) {
            throw new RuntimeException("Patient code already exists: " + requestDTO.getPatientCode());
        }

        Patient patient = new Patient();
        mapDTOToEntity(requestDTO, patient);
        patient.setCreatedBy(createdBy);

        Patient savedPatient = patientRepository.save(patient);
        return mapEntityToDTO(savedPatient);
    }

    public PatientResponseDTO updatePatient(Long patientId, PatientRequestDTO requestDTO, String updatedBy) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        if (!patient.getPatientCode().equals(requestDTO.getPatientCode()) &&
            patientRepository.existsByPatientCode(requestDTO.getPatientCode())) {
            throw new RuntimeException("Patient code already exists: " + requestDTO.getPatientCode());
        }

        mapDTOToEntity(requestDTO, patient);
        patient.setUpdatedBy(updatedBy);

        Patient updatedPatient = patientRepository.save(patient);
        return mapEntityToDTO(updatedPatient);
    }

    public PatientResponseDTO getPatientById(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));
        return mapEntityToDTO(patient);
    }

    public PatientResponseDTO getPatientByCode(String patientCode) {
        Patient patient = patientRepository.findByPatientCode(patientCode)
                .orElseThrow(() -> new RuntimeException("Patient not found with code: " + patientCode));
        return mapEntityToDTO(patient);
    }

    public List<PatientResponseDTO> getActivePatients() {
        List<Patient> patients = patientRepository.findByIsActiveTrueAndIsDeletedFalse();
        return patients.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    public List<PatientResponseDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findByIsDeletedFalse();
        return patients.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    public List<PatientResponseDTO> searchPatients(String keyword) {
        List<Patient> patients = patientRepository.searchPatients(keyword);
        return patients.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    public void deletePatient(Long patientId, String deletedBy) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        patient.setIsDeleted(true);
        patient.setIsActive(false);
        patient.setUpdatedBy(deletedBy);
        patient.setUpdatedAt(LocalDateTime.now());

        patientRepository.save(patient);
    }

    private void mapDTOToEntity(PatientRequestDTO dto, Patient entity) {
        entity.setPatientCode(dto.getPatientCode());
        entity.setFullName(dto.getFullName());
        entity.setDateOfBirth(dto.getDateOfBirth());
        entity.setGender(dto.getGender());
        entity.setAddress(dto.getAddress());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setEmail(dto.getEmail());
        entity.setIdentityNumber(dto.getIdentityNumber());
        entity.setEmergencyContactName(dto.getEmergencyContactName());
        entity.setEmergencyContactPhone(dto.getEmergencyContactPhone());
        entity.setIsActive(dto.getIsActive());
    }

    private PatientResponseDTO mapEntityToDTO(Patient entity) {
        PatientResponseDTO dto = new PatientResponseDTO();
        dto.setPatientId(entity.getPatientId());
        dto.setPatientCode(entity.getPatientCode());
        dto.setFullName(entity.getFullName());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setGender(entity.getGender());
        dto.setAddress(entity.getAddress());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setEmail(entity.getEmail());
        dto.setIdentityNumber(entity.getIdentityNumber());
        dto.setEmergencyContactName(entity.getEmergencyContactName());
        dto.setEmergencyContactPhone(entity.getEmergencyContactPhone());
        dto.setIsActive(entity.getIsActive());
        dto.setIsDeleted(entity.getIsDeleted());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());
        return dto;
    }
}

