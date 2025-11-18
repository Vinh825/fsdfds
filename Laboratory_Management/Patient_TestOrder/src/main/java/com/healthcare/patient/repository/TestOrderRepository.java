package com.healthcare.patient.repository;

import com.healthcare.patient.entity.TestOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestOrderRepository extends JpaRepository<TestOrder, Long> {
    List<TestOrder> findAllByIsDeletedFalseOrderByCreatedAtDesc();
    boolean existsByOrderNumber(String orderNumber);
}
