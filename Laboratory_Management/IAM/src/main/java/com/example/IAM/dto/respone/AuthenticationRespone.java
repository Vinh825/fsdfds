package com.example.IAM.dto.respone;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRespone {
    String token;
    boolean authenticated;
    UUID userId;
    String username;
    boolean mustChangePassword;
    Set<String> roleCodes;
    Set<String> privilegeCodes;
}