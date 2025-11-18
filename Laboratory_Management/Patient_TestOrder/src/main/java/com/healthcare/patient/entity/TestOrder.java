package com.healthcare.patient.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "test_orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TestOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_order_id")
    private Long id; // ✅ BIGINT

    @Column(name = "order_number", nullable = false, unique = true, length = 40)
    private String orderNumber;

    @Column(name = "patient_id", nullable = false)
    private Long patientId; // ✅ BIGINT

    @Embedded
    private PatientSnapshot patient;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String status;

    private LocalDateTime runDate;
    private String createdBy;
    private String runBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Boolean isDeleted;

    @PrePersist
    public void prePersist() {
        if (isDeleted == null) isDeleted = false;
        if (status == null) status = "PENDING";
    }
}
