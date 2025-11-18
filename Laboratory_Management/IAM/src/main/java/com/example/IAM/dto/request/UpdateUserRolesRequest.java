package com.example.IAM.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class UpdateUserRolesRequest {

    @NotNull
    private Set<String> roleCodes;
}
