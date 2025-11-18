package com.healthcare.patient.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTestOrderRequest {
    @NotNull
    private Long patientId;
}
