package com.healthcare.patient.repository;

import com.healthcare.patient.entity.Patient;
import com.healthcare.patient.entity.PatientMedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientMedicalRecordRepository extends JpaRepository<PatientMedicalRecord, Long> {

    List<PatientMedicalRecord> findByPatientAndIsDeletedFalse(Patient patient);

    List<PatientMedicalRecord> findByPatientAndIsCurrentTrueAndIsDeletedFalse(Patient patient);

    Optional<PatientMedicalRecord> findByPatientMedicalRecordIdAndIsDeletedFalse(Long recordId);

    @Query("SELECT r FROM PatientMedicalRecord r WHERE r.patient.patientId = :patientId " +
           "AND r.isDeleted = false ")
    List<PatientMedicalRecord> findByPatientIdOrderByRecordDateDesc(@Param("patientId") Long patientId);


}


