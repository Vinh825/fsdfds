package com.healthcare.patient.service;

import com.healthcare.patient.entity.TestOrderAuditLog;
import com.healthcare.patient.repository.TestOrderAuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestOrderAuditLogService {

    private final TestOrderAuditLogRepository repo;

    public void log(Long orderId, String action, String actor, String details) {
        var log = TestOrderAuditLog.builder()
                .testOrderId(orderId)
                .action(action)
                .actor(actor)
                .details(details)
                .build();
        repo.save(log);
    }
}
