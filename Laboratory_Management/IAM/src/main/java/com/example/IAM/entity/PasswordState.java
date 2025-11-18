package com.example.IAM.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "password_states")
public class PasswordState {

    @Id
    @Column(name = "user_id")
    UUID userId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_pwdstate_user"))
    User user;

    @Column(name = "password_changed_at", nullable = false)
    OffsetDateTime passwordChangedAt;

    @Column(name = "password_expiry_date", nullable = false)
    OffsetDateTime passwordExpiryDate;


    public boolean isExpired() {
        return passwordExpiryDate != null && passwordExpiryDate.isBefore(OffsetDateTime.now());
    }
}