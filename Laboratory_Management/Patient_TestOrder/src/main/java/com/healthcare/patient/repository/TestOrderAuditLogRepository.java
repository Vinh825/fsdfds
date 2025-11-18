package com.healthcare.patient.repository;

import com.healthcare.patient.entity.TestOrderAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestOrderAuditLogRepository extends JpaRepository<TestOrderAuditLog, Long> {
}
