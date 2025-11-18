package com.example.IAM.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "role_privileges",
        uniqueConstraints = @UniqueConstraint(name = "uk_role_privilege", columnNames = {"role_id", "privilege"}))

public class RolePrivilege {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_role_privileges_role"))
    Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "privilege", nullable = false, length = 64)
    Privilege privilege;

    @Builder.Default
    @Column(name = "permitted", nullable = false)
    Boolean permitted = Boolean.TRUE;
}
