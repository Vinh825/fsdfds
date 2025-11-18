package com.example.IAM.controller;

import com.example.IAM.dto.request.CreateRoleRequest;
import com.example.IAM.dto.request.UpdateRoleRequest;
import com.example.IAM.dto.respone.ApiResponse;
import com.example.IAM.dto.respone.PageResponse;
import com.example.IAM.dto.respone.RoleResponse;
import com.example.IAM.entity.Privilege;
import com.example.IAM.service.RoleService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {

    RoleService roleService;

    @PreAuthorize("hasAuthority('ROLE_CREATE')")
    @PostMapping("/create")
    public ApiResponse<RoleResponse> createRole(@RequestBody @Valid CreateRoleRequest request) {
        var result = roleService.create(request);
        return ApiResponse.<RoleResponse>builder().result(result).build();
    }

    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    @GetMapping
    public ApiResponse<List<RoleResponse>> getAllRoles() {
        var result = roleService.getAllRoles();
        return ApiResponse.<List<RoleResponse>>builder().result(result).build();
    }

    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    @GetMapping("/{roleCode}")
    public ApiResponse<RoleResponse> getRoleByRoleCode(@PathVariable String roleCode) {
        var result = roleService.getRoleByRoleCode(roleCode);
        return ApiResponse.<RoleResponse>builder().result(result).build();
    }

    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    @PutMapping("/{roleCode}")
    public ApiResponse<RoleResponse> updateRole(@PathVariable String roleCode,
            @RequestBody @Valid UpdateRoleRequest request) {
        var result = roleService.update(roleCode, request);
        return ApiResponse.<RoleResponse>builder().result(result).build();
    }

    @PreAuthorize("hasAuthority('ROLE_DELETE')")
    @DeleteMapping("/{roleCode}")
    public ApiResponse<Void> deleteRole(@PathVariable String roleCode) {
        roleService.delete(roleCode);
        return ApiResponse.<Void>builder().build();
    }

    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    @GetMapping("/list")
    public ApiResponse<PageResponse<RoleResponse>> listRoles(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String privilege,
            @RequestParam(defaultValue = "name,asc") String sort) {
        String prop = "name";
        String dir = "asc";
        if (sort != null && !sort.isBlank()) {
            String[] parts = sort.split(",");
            if (parts.length >= 1 && !parts[0].isBlank())
                prop = parts[0];
            if (parts.length >= 2 && (parts[1].equalsIgnoreCase("asc") || parts[1].equalsIgnoreCase("desc")))
                dir = parts[1];
        }
        String dirValue = (dir == null || dir.isBlank()) ? "asc" : dir;
        Sort.Direction direction = Sort.Direction.fromString(dirValue);
        String normalizedProp = prop == null ? "" : prop.trim().toLowerCase(Locale.ROOT);
        Sort sortSpec;
        switch (normalizedProp) {
            case "rolecode" -> sortSpec = direction.isAscending()
                    ? Sort.by("roleCode").ascending()
                    : Sort.by("roleCode").descending();
            case "createdat" -> sortSpec = direction.isAscending()
                    ? Sort.by("createdAt").ascending()
                    : Sort.by("createdAt").descending();
            default -> sortSpec = direction.isAscending()
                    ? Sort.by("name").ascending()
                    : Sort.by("name").descending();
        }
        var result = roleService.list(page, size, q, privilege, sortSpec);
        return ApiResponse.<PageResponse<RoleResponse>>builder().result(result).build();
    }

    @PreAuthorize("hasAuthority('ROLE_CREATE') or hasAuthority('ROLE_UPDATE')")
    @GetMapping("/privileges")
    public ApiResponse<List<String>> getPrivileges() {
        List<String> items = Arrays.stream(Privilege.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ApiResponse.<List<String>>builder().result(items).build();
    }
}
