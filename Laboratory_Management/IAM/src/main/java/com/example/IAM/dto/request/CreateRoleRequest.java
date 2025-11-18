package com.example.IAM.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.Set;

public record CreateRoleRequest(
        @NotBlank String name,
        @NotBlank
        @Pattern(regexp="^[A-Z0-9_\\-]{3,50}$", message="roleCode must be 3-50 chars [A-Z0-9_-]")
        String roleCode,
        String description,
        Set<PrivilegeEntry> privileges // optional; default READ_ONLY: true
) {}    