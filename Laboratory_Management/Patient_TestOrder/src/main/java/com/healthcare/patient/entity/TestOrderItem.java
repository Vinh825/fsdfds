package com.healthcare.patient.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "test_order_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TestOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_order_item_id")
    private Long id; // ✅ BIGINT

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_order_id", nullable = false)
    private TestOrder order; // ✅ FK BIGINT → test_orders.test_order_id

    @Column(name = "test_type_id", nullable = false)
    private Long testTypeId;

    @Column(name = "test_code")
    private String testCode;

    @Column(name = "test_name")
    private String testName;

    @Column(name = "priority")
    private String priority;

    @Column(name = "status")
    private String status;

    @Column(name = "specimen_type")
    private String specimenType;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @PrePersist
    public void prePersist() {
        if (isDeleted == null) isDeleted = false;
        if (status == null) status = "PENDING";
        if (priority == null) priority = "Normal";
    }
}
