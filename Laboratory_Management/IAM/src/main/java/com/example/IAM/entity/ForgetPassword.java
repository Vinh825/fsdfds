package com.example.IAM.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RedisHash("forgot_otp")
public class ForgetPassword {
    @Id
    String email;

    String codeHash;

    @Builder.Default
    Integer attempts = 0;

    @Builder.Default
    Boolean used = false;

    OffsetDateTime createdAt;
    OffsetDateTime usedAt;

    @TimeToLive(unit = TimeUnit.SECONDS)
    @Builder.Default
    Long ttlSeconds = 300L;
}
