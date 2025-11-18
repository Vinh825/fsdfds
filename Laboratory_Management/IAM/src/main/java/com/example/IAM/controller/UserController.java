package com.example.IAM.controller;

import com.example.IAM.common.exception.AppException;
import com.example.IAM.common.exception.ErrorCode;
import com.example.IAM.dto.request.ChangcePasswordRequest;
import com.example.IAM.dto.request.CreateUserRequest;
import com.example.IAM.dto.request.UpdateUserLockRequest;
import com.example.IAM.dto.request.UpdateUserRolesRequest;
import com.example.IAM.dto.request.UpdateUserRequest;
import com.example.IAM.dto.respone.ApiResponse;
import com.example.IAM.dto.respone.ChangcePasswordRespone;
import com.example.IAM.dto.respone.PageResponse;
import com.example.IAM.dto.respone.UserResponse;
import com.example.IAM.entity.Gender;
import com.example.IAM.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {

    UserService userService;

    @PostMapping("/change-password/{id}")
    public ApiResponse<ChangcePasswordRespone> changePassword(
            @PathVariable UUID id,
            @Valid @RequestBody ChangcePasswordRequest req) {
        var result = userService.changePassword(id, req);
        return ApiResponse.<ChangcePasswordRespone>builder().result(result).build();
    }

    @PreAuthorize("hasAuthority('USER_CREATE')")
    @PostMapping("/create")
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        var result = userService.createUser(request);
        return ApiResponse.<UserResponse>builder().result(result).build();
    }


    @PreAuthorize("hasAuthority('USER_VIEW')")
    @GetMapping
    public ApiResponse<PageResponse<UserResponse>> listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String gender,
            @RequestParam(name = "roleCode", required = false) String roleCode) {
        Gender genderEnum = null;
        if (gender != null && !gender.isBlank()) {
            try {
                genderEnum = Gender.valueOf(gender.trim().toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ex) {
                throw new AppException(ErrorCode.INVALID_KEY);
            }
        }
        var result = userService.list(page, size, q, genderEnum, roleCode);
        return ApiResponse.<PageResponse<UserResponse>>builder().result(result).build();
    }

//    @PreAuthorize("hasAuthority('USER_VIEW')")
    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable UUID id) {
        var result = userService.getUserById(id);
        return ApiResponse.<UserResponse>builder().result(result).build();
    }


    @PreAuthorize("hasAuthority('USER_MODIFY')")
    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable UUID id, @RequestBody @Valid UpdateUserRequest request) {
        var result = userService.updateUser(id, request);
        return ApiResponse.<UserResponse>builder().result(result).build();
    }

    @PreAuthorize("hasAuthority('USER_DELETE')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ApiResponse.<Void>builder().build();
    }


    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    @PutMapping("/{id}/roles")
    public ApiResponse<UserResponse> updateUserRoles(@PathVariable UUID id,
            @RequestBody @Valid UpdateUserRolesRequest request) {
        var result = userService.updateUserRoles(id, request.getRoleCodes());
        return ApiResponse.<UserResponse>builder().result(result).build();
    }


    @PreAuthorize("hasAuthority('USER_LOCK_UNLOCK')")
    @PatchMapping("/{id}/lock")
    public ApiResponse<UserResponse> updateLockStatus(@PathVariable UUID id,
            @RequestBody @Valid UpdateUserLockRequest request) {
        var result = userService.updateUserLock(id, request.getLocked());
        return ApiResponse.<UserResponse>builder().result(result).build();
    }
}
