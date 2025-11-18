package com.healthcare.patient.controller;

import com.healthcare.patient.common.ApiResponse;
import com.healthcare.patient.dto.CreateTestOrderRequest;
import com.healthcare.patient.dto.TestOrderResponseDTO;
import com.healthcare.patient.dto.item.*;
import com.healthcare.patient.service.TestOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test-orders")
@RequiredArgsConstructor
public class TestOrderController {

    private final TestOrderService service;

    @GetMapping
    @PreAuthorize("hasAuthority('TEST_ORDER_REVIEW')")
    public ApiResponse<List<TestOrderResponseDTO>> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('TEST_ORDER_REVIEW')")
    public ApiResponse<TestOrderResponseDTO> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('TEST_ORDER_CREATE')")
    public ApiResponse<TestOrderResponseDTO> create(@Valid @RequestBody CreateTestOrderRequest req) {
        return service.create(req);
    }

    @PostMapping("/{id}/complete")
    @PreAuthorize("hasAuthority('TEST_ORDER_MODIFY')")
    public ApiResponse<TestOrderResponseDTO> complete(@PathVariable Long id) {
        return service.markCompleted(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TEST_ORDER_DELETE')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        return service.delete(id);
    }

    // === Items ===
    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasAuthority('TEST_ORDER_REVIEW')")
    public ApiResponse<List<TestOrderItemResponse>> listItems(@PathVariable Long orderId) {
        return service.listItems(orderId);
    }

    @PostMapping("/{orderId}/items")
    @PreAuthorize("hasAuthority('TEST_ORDER_MODIFY')")
    public ApiResponse<TestOrderItemResponse> addItem(@PathVariable Long orderId,
            @Valid @RequestBody CreateTestOrderItemRequest dto) {
        return service.addItem(orderId, dto);
    }

    @PutMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasAuthority('TEST_ORDER_MODIFY')")
    public ApiResponse<TestOrderItemResponse> updateItem(@PathVariable Long orderId,
            @PathVariable Long itemId,
            @RequestBody UpdateTestOrderItemRequest dto) {
        return service.updateItem(orderId, itemId, dto);
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasAuthority('TEST_ORDER_DELETE')")
    public ApiResponse<Void> deleteItem(@PathVariable Long orderId, @PathVariable Long itemId) {
        return service.deleteItem(orderId, itemId);
    }
}
