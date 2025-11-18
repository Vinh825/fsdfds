package com.healthcare.patient.repository;

import com.healthcare.patient.entity.TestOrderItem;
import com.healthcare.patient.entity.TestOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestOrderItemRepository extends JpaRepository<TestOrderItem, Long> {
    List<TestOrderItem> findAllByOrderAndIsDeletedFalseOrderByCreatedAtAsc(TestOrder order);
}
