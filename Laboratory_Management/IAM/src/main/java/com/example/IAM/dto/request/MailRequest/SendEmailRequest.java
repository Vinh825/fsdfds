package com.example.IAM.dto.request.MailRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendEmailRequest {
    @Valid
    @NotNull
    Recipient to;

    String subject;

    String htmlContent;
}
