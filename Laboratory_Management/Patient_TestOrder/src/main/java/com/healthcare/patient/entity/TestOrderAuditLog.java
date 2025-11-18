package com.healthcare.patient.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "test_order_audit_logs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TestOrderAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_log_id")
    private Long id; // BIGINT

    @Column(name = "test_order_id", nullable = false)
    private Long testOrderId; // FK -> test_orders.test_order_id (BIGINT)

    @Column(name = "action", nullable = false, length = 50)
    private String action; // CREATE / UPDATE / COMPLETE / DELETE / ADD_ITEM / UPDATE_ITEM / DELETE_ITEM

    @Column(name = "actor", length = 100)
    private String actor; // ai thực hiện (createdBy / runBy / deletedBy / v.v.)

    @Column(name = "details", columnDefinition = "TEXT")
    private String details; // mô tả chi tiết thay đổi

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
