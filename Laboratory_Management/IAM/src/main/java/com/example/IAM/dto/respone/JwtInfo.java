package com.example.IAM.dto.respone;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtInfo {
    String jwtId;
    Date issueTime;
    Date expiredTime;
}
