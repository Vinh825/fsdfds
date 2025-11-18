package com.example.IAM.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.Set;

public record UpdateRoleRequest(
        @NotBlank String name,
        String description,
        Set<PrivilegeEntry> privileges
) {}