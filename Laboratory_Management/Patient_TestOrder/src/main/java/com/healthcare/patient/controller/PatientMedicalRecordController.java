package com.healthcare.patient.controller;

import com.healthcare.patient.dto.PatientMedicalRecordRequestDTO;
import com.healthcare.patient.dto.PatientMedicalRecordResponseDTO;
import com.healthcare.patient.service.PatientMedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient-medical-records")

public class PatientMedicalRecordController {

    @Autowired
    private PatientMedicalRecordService recordService;

    @PostMapping
    public ResponseEntity<PatientMedicalRecordResponseDTO> createRecord(
            @Valid @RequestBody PatientMedicalRecordRequestDTO requestDTO,
            @RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {
        PatientMedicalRecordResponseDTO response = recordService.createRecord(requestDTO, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientMedicalRecordResponseDTO> updateRecord(
            @PathVariable Long id,
            @Valid @RequestBody PatientMedicalRecordRequestDTO requestDTO,
            @RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {
        PatientMedicalRecordResponseDTO response = recordService.updateRecord(id, requestDTO, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientMedicalRecordResponseDTO> getRecordById(@PathVariable Long id) {
        PatientMedicalRecordResponseDTO response = recordService.getRecordById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PatientMedicalRecordResponseDTO>> getRecordsByPatientId(
            @PathVariable Long patientId) {
        List<PatientMedicalRecordResponseDTO> response = recordService.getRecordsByPatientId(patientId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patient/{patientId}/current")
    public ResponseEntity<List<PatientMedicalRecordResponseDTO>> getCurrentRecordsByPatientId(
            @PathVariable Long patientId) {
        List<PatientMedicalRecordResponseDTO> response = recordService.getCurrentRecordsByPatientId(patientId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {
        recordService.deleteRecord(id, userId);
        return ResponseEntity.noContent().build();
    }
}

