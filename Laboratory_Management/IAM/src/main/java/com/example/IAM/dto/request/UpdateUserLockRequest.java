package com.example.IAM.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserLockRequest {

    @NotNull
    private Boolean locked;
}
