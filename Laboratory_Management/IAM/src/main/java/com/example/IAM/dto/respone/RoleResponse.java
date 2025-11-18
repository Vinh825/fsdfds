package com.example.IAM.dto.respone;

import com.example.IAM.entity.Privilege;
import java.time.OffsetDateTime;
import java.util.List;

public record RoleResponse(
        String roleCode,
        String name,
        String description,
        List<PrivilegeGrant> privileges,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public record PrivilegeGrant(Privilege privilege, boolean permitted) {}
}