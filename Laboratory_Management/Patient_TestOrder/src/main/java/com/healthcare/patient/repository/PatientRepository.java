package com.healthcare.patient.repository;

import com.healthcare.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByPatientCode(String patientCode);

    List<Patient> findByIsActiveTrueAndIsDeletedFalse();

    List<Patient> findByIsDeletedFalse();

    @Query("SELECT p FROM Patient p WHERE p.isDeleted = false AND " +
           "(LOWER(p.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "p.patientCode LIKE CONCAT('%', :keyword, '%'))")
    List<Patient> searchPatients(@Param("keyword") String keyword);

    boolean existsByPatientCode(String patientCode);
}


