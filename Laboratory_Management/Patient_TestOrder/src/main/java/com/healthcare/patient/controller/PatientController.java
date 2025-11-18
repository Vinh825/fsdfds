package com.healthcare.patient.controller;

import com.healthcare.patient.dto.PatientRequestDTO;
import com.healthcare.patient.dto.PatientResponseDTO;
import com.healthcare.patient.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.healthcare.patient.utils.SecurityUtils;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    @PreAuthorize("hasAuthority('TEST_ORDER_CREATE')")
    public ResponseEntity<PatientResponseDTO> createPatient(
            @Valid @RequestBody PatientRequestDTO requestDTO) {
        String actor = SecurityUtils.currentUsername();
        PatientResponseDTO response = patientService.createPatient(requestDTO, actor);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TEST_ORDER_MODIFY')")
    public ResponseEntity<PatientResponseDTO> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody PatientRequestDTO requestDTO) {
        String actor = SecurityUtils.currentUsername();
        PatientResponseDTO response = patientService.updatePatient(id, requestDTO, actor);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('TEST_ORDER_REVIEW')")
    public ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable Long id) {
        PatientResponseDTO response = patientService.getPatientById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/code/{code}")
    @PreAuthorize("hasAuthority('TEST_ORDER_REVIEW')")
    public ResponseEntity<PatientResponseDTO> getPatientByCode(@PathVariable String code) {
        PatientResponseDTO response = patientService.getPatientByCode(code);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('TEST_ORDER_REVIEW')")
    public ResponseEntity<List<PatientResponseDTO>> getActivePatients() {
        List<PatientResponseDTO> response = patientService.getActivePatients();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('TEST_ORDER_REVIEW')")
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {
        List<PatientResponseDTO> response = patientService.getAllPatients();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('TEST_ORDER_REVIEW')")
    public ResponseEntity<List<PatientResponseDTO>> searchPatients(@RequestParam String keyword) {
        List<PatientResponseDTO> response = patientService.searchPatients(keyword);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TEST_ORDER_DELETE')")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        String actor = SecurityUtils.currentUsername();
        patientService.deletePatient(id, actor);
        return ResponseEntity.noContent().build();
    }
}
