package com.example.IAM.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Email
    @Column(nullable = false, length = 255)
    String email;

    String password;

    @Column(name = "first_name", nullable = false, length = 100)
    String firstName;

    @Column(name = "last_name", nullable = false, length = 155)
    String lastName;

    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
    @Column(name = "username", nullable = false, unique = true, length = 50)
    String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    Gender gender;

    @Pattern(regexp = "^(0|\\+84)(3[2-9]|5[25689]|7[06-9]|8[1-689]|9[0-46-9])\\d{7}$", message = "Invalid mobile phone number")
    @Column(name = "phone", nullable = false, length = 11)
    String phoneNumber;

    @Column(name = "full_name") // Tên cột này phải khớp với database
    String fullName;

    @Column
    String address;

    @Column(name = "identity_number", nullable = false, length = 32)
    String identityNumber;

    @Column(name = "date_of_birth", nullable = false)
    LocalDate dateOfBirth;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), uniqueConstraints = @UniqueConstraint(name = "uk_user_role", columnNames = {
            "user_id", "role_id" }))
    @Builder.Default
    Set<Role> roles = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    OffsetDateTime createdAt;

    @Column(name = "created_by")
    String createdBy;

    @UpdateTimestamp
    @Column(name = "updated_at")
    OffsetDateTime updatedAt;

    @Column(name = "updated_by")
    String updatedBy;

    @Column(name = "deleted_at")
    OffsetDateTime deletedAt;

    @Column(name = "is_locked")
    @Builder.Default
    Boolean isLocked = false;

    @Column(name = "failed_login_attempts")
    @Builder.Default
    Integer failedLoginAttempts = 0;

    @Column(name = "must_change_password")
    @Builder.Default
    Boolean mustChangePassword = true;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    PasswordState passwordState;

    @Transient
    public boolean isPasswordExpired() {
        return passwordState != null && passwordState.isExpired();
    }

    @Column(name = "last_login_date")
    OffsetDateTime lastLoginDate;

    @Column(name = "is_active")
    @Builder.Default
    Boolean isActive = true;

    @Column(name = "deleted")
    @Builder.Default
    Boolean deleted = false;

    public boolean isDeleted() {
        return Boolean.TRUE.equals(deleted) || deletedAt != null;
    }

    public boolean isAccountLocked() {
        return isLocked != null && isLocked;
    }

    public boolean mustChangePassword() {
        return Boolean.TRUE.equals(mustChangePassword);
    }

    @PrePersist
    void ensureIdentityNumber() {
        if (identityNumber == null || identityNumber.isBlank()) {
            String uuidPart = UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
            identityNumber = "HL-" + uuidPart;
        }
    }

    @Transient
    public Integer getAge() {
        if (dateOfBirth == null)
            return null;
        LocalDate today = LocalDate.now();
        return Period.between(dateOfBirth, today).getYears();
    }

}
