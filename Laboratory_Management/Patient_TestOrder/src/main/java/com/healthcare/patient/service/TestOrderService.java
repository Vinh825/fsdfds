package com.healthcare.patient.service;

import com.healthcare.patient.common.ApiResponse;
import com.healthcare.patient.entity.Patient;
import com.healthcare.patient.repository.PatientRepository;

import com.healthcare.patient.dto.CreateTestOrderRequest;
import com.healthcare.patient.dto.TestOrderResponseDTO;

import com.healthcare.patient.dto.item.CreateTestOrderItemRequest;
import com.healthcare.patient.dto.item.TestOrderItemResponse;
import com.healthcare.patient.dto.item.UpdateTestOrderItemRequest;

import com.healthcare.patient.entity.PatientSnapshot;
import com.healthcare.patient.entity.TestOrder;
import com.healthcare.patient.entity.TestOrderItem;

import com.healthcare.patient.mapper.TestOrderItemMapper;

import com.healthcare.patient.repository.TestOrderItemRepository;
import com.healthcare.patient.repository.TestOrderRepository;
import com.healthcare.patient.utils.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestOrderService {

        private final TestOrderRepository testOrderRepository;
        private final TestOrderItemRepository itemRepository;
        private final PatientRepository patientRepository;
        private final TestOrderAuditLogService audit; // service ghi vào test_order_audit_logs

        // ===================== ORDERS =====================

        public ApiResponse<List<TestOrderResponseDTO>> getAll() {
                var list = testOrderRepository.findAllByIsDeletedFalseOrderByCreatedAtDesc()
                                .stream().map(this::toResponse).toList();
                return ApiResponse.success(list, "Fetched test orders successfully");
        }

        public ApiResponse<TestOrderResponseDTO> getById(Long id) {
                var order = testOrderRepository.findById(id)
                                .filter(o -> !Boolean.TRUE.equals(o.getIsDeleted()))
                                .orElseThrow(() -> new EntityNotFoundException("Test order not found"));
                return ApiResponse.success(toResponse(order), "Fetched test order successfully");
        }

        public ApiResponse<TestOrderResponseDTO> create(CreateTestOrderRequest req) {
                Patient p = patientRepository.findById(req.getPatientId())
                                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));

                PatientSnapshot snap = PatientSnapshot.builder()
                                .patientCode(p.getPatientCode())
                                .fullName(p.getFullName())
                                .dateOfBirth(p.getDateOfBirth())
                                .gender(p.getGender())
                                .address(p.getAddress())
                                .phoneNumber(p.getPhoneNumber())
                                .email(p.getEmail())
                                .identityNumber(p.getIdentityNumber())
                                .build();

                Integer age = calcAge(p.getDateOfBirth());

                String actor = SecurityUtils.currentUsername();

                TestOrder order = TestOrder.builder()
                                .patientId(p.getPatientId()) // BIGINT
                                .patient(snap) // snapshot embed
                                .age(age)
                                .status("PENDING")
                                .createdBy(actor)
                                .orderNumber(generateUniqueOrderNumber())
                                .isDeleted(false)
                                .build();

                var saved = testOrderRepository.save(order);

                // Audit
                audit.log(
                                saved.getId(),
                                "CREATE",
                                actor,
                                "orderNumber=" + saved.getOrderNumber()
                                                + ", patientId=" + saved.getPatientId()
                                                + ", status=" + saved.getStatus());

                return ApiResponse.success(toResponse(saved), "Created test order successfully");
        }

        public ApiResponse<TestOrderResponseDTO> markCompleted(Long id) {
                var order = testOrderRepository.findById(id)
                                .filter(o -> !Boolean.TRUE.equals(o.getIsDeleted()))
                                .orElseThrow(() -> new EntityNotFoundException("Test order not found"));

                String actor = SecurityUtils.currentUsername();
                String oldStatus = order.getStatus();
                order.setStatus("COMPLETED");
                order.setRunBy(actor);
                order.setRunDate(LocalDateTime.now());
                var saved = testOrderRepository.save(order);

                // Audit
                audit.log(
                                saved.getId(),
                                "COMPLETE",
                                actor,
                                "status: " + oldStatus + " -> " + saved.getStatus() + ", runDate="
                                                + saved.getRunDate());

                return ApiResponse.success(toResponse(saved), "Marked order as COMPLETED");
        }

        public ApiResponse<Void> delete(Long id) {
                var order = testOrderRepository.findById(id)
                                .filter(o -> !Boolean.TRUE.equals(o.getIsDeleted()))
                                .orElseThrow(() -> new EntityNotFoundException("Test order not found"));

                order.setIsDeleted(true);
                testOrderRepository.save(order);

                String actor = SecurityUtils.currentUsername();
                // Audit
                audit.log(
                                order.getId(),
                                "DELETE",
                                actor,
                                "soft delete");

                return ApiResponse.success(null, "Deleted test order by " + actor);
        }

        // ===================== ITEMS =====================

        public ApiResponse<List<TestOrderItemResponse>> listItems(Long orderId) {
                var order = testOrderRepository.findById(orderId)
                                .filter(o -> !Boolean.TRUE.equals(o.getIsDeleted()))
                                .orElseThrow(() -> new EntityNotFoundException("Test order not found"));

                var items = itemRepository.findAllByOrderAndIsDeletedFalseOrderByCreatedAtAsc(order)
                                .stream().map(TestOrderItemMapper::toResponse).toList();

                return ApiResponse.success(items, "Fetched order items");
        }

        public ApiResponse<TestOrderItemResponse> addItem(Long orderId, CreateTestOrderItemRequest dto) {
                var order = testOrderRepository.findById(orderId)
                                .filter(o -> !Boolean.TRUE.equals(o.getIsDeleted()))
                                .orElseThrow(() -> new EntityNotFoundException("Test order not found"));

                String actor = SecurityUtils.currentUsername();

                var item = TestOrderItem.builder()
                                .order(order)
                                .testTypeId(dto.getTestTypeId()) // bắt buộc vì DB NOT NULL
                                .testName(dto.getTestName())
                                .testCode(dto.getTestCode())
                                .priority(dto.getPriority())
                                .specimenType(dto.getSpecimenType())
                                .notes(dto.getNotes())
                                .isDeleted(false)
                                .build();

                var saved = itemRepository.save(item);

                // Audit
                audit.log(
                                order.getId(),
                                "ADD_ITEM",
                                actor,
                                "itemId=" + saved.getId()
                                                + ", testTypeId=" + saved.getTestTypeId()
                                                + ", testName=" + saved.getTestName());

                return ApiResponse.success(TestOrderItemMapper.toResponse(saved), "Added order item");
        }

        public ApiResponse<TestOrderItemResponse> updateItem(Long orderId, Long itemId,
                        UpdateTestOrderItemRequest dto) {
                var order = testOrderRepository.findById(orderId)
                                .filter(o -> !Boolean.TRUE.equals(o.getIsDeleted()))
                                .orElseThrow(() -> new EntityNotFoundException("Test order not found"));

                var item = itemRepository.findById(itemId)
                                .filter(i -> !Boolean.TRUE.equals(i.getIsDeleted()))
                                .orElseThrow(() -> new EntityNotFoundException("Order item not found"));

                if (!item.getOrder().getId().equals(order.getId())) {
                        throw new EntityNotFoundException("Item does not belong to this order");
                }

                var beforeStatus = item.getStatus();
                TestOrderItemMapper.applyUpdate(item, dto);
                var saved = itemRepository.save(item);

                String actor = SecurityUtils.currentUsername();
                // Audit
                audit.log(
                                order.getId(),
                                "UPDATE_ITEM",
                                actor,
                                "itemId=" + saved.getId()
                                                + (dto.getStatus() != null
                                                                ? (", status: " + beforeStatus + " -> "
                                                                                + saved.getStatus())
                                                                : ""));

                return ApiResponse.success(TestOrderItemMapper.toResponse(saved), "Updated order item");
        }

        public ApiResponse<Void> deleteItem(Long orderId, Long itemId) {
                var order = testOrderRepository.findById(orderId)
                                .filter(o -> !Boolean.TRUE.equals(o.getIsDeleted()))
                                .orElseThrow(() -> new EntityNotFoundException("Test order not found"));

                var item = itemRepository.findById(itemId)
                                .filter(i -> !Boolean.TRUE.equals(i.getIsDeleted()))
                                .orElseThrow(() -> new EntityNotFoundException("Order item not found"));

                if (!item.getOrder().getId().equals(order.getId())) {
                        throw new EntityNotFoundException("Item does not belong to this order");
                }

                item.setIsDeleted(true);
                itemRepository.save(item);

                String actor = SecurityUtils.currentUsername();
                // Audit
                audit.log(
                                order.getId(),
                                "DELETE_ITEM",
                                actor,
                                "itemId=" + itemId + ", soft delete");

                return ApiResponse.success(null, "Deleted order item");
        }

        // ===================== HELPERS =====================

        private TestOrderResponseDTO toResponse(TestOrder e) {
                var snap = e.getPatient();
                return TestOrderResponseDTO.builder()
                                .id(e.getId()) // Long
                                .patientName(snap != null ? snap.getFullName() : null)
                                .age(e.getAge())
                                .gender(snap != null ? snap.getGender() : null)
                                .phoneNumber(snap != null ? snap.getPhoneNumber() : null)
                                .status(e.getStatus())
                                .createdAt(e.getCreatedAt())
                                .createdBy(e.getCreatedBy())
                                .runDate(e.getRunDate())
                                .runBy(e.getRunBy())
                                .build();
        }

        private Integer calcAge(LocalDate dob) {
                if (dob == null)
                        return null;
                return Period.between(dob, LocalDate.now()).getYears();
        }

        private String generateUniqueOrderNumber() {
                String prefix = "TO-" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + "-";
                String code;
                int guard = 0;
                do {
                        code = prefix + RandomStringUtils.randomAlphanumeric(6).toUpperCase();
                        guard++;
                } while (testOrderRepository.existsByOrderNumber(code) && guard < 10);
                return code;
        }
}
