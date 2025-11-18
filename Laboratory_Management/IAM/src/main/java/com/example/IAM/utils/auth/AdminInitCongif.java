package com.example.IAM.utils.auth;

import com.example.IAM.entity.*;
import com.example.IAM.repository.RolePrivilegeRepository;
import com.example.IAM.repository.RoleRepository;
import com.example.IAM.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class AdminInitCongif {

    final PasswordEncoder passwordEncoder;
    final RoleRepository roleRepository;
    final RolePrivilegeRepository rolePrivilegeRepository;
    final UserRepository userRepository;

    public AdminInitCongif(PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository,
                           RolePrivilegeRepository rolePrivilegeRepository,
                           UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.rolePrivilegeRepository = rolePrivilegeRepository;
        this.userRepository = userRepository;
    }

    @Value("${info.admin.enabled}")
    boolean adminEnabled;

    @Value("${info.admin.role-code}")
    String adminRoleCode;

    @Value("${info.admin.role-name}")
    String adminRoleName;

    @Value("${info.admin.role-description}")
    String adminRoleDescription;

    @Value("${info.admin.username}")
    String adminUsername;

    @Value("${info.admin.email}")
    String adminEmail;

    @Value("${info.admin.first-name}")
    String adminFirstName;

    @Value("${info.admin.last-name}")
    String adminLastName;

    @Value("${info.admin.gender}")
    String adminGender;

    @Value("${info.admin.phone-number}")
    String adminPhoneNumber;

    @Value("${info.admin.date-of-birth}")
    String adminDateOfBirth;

    @Value("${info.admin.address}")
    String adminAddress;

    @Value("${info.admin.password}")
    String adminPassword;

    @SuppressWarnings("null")
    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            if (!adminEnabled) {
                log.info("Admin creation is disabled by configuration (info.admin.enabled=false).");
                return;
            }

            Optional<Role> existingRoleOpt = roleRepository.findByRoleCodeAndDeletedAtIsNull(adminRoleCode);
            Role adminRole;
            if (existingRoleOpt.isEmpty()) {
                log.info("Role {} does not exist, initializing...", adminRoleCode);
                adminRole = Role.builder()
                        .name(adminRoleName)
                        .roleCode(adminRoleCode)
                        .description(adminRoleDescription)
                        .build();
                adminRole = roleRepository.save(adminRole);
                log.info("Role {} has been saved.", adminRoleCode);
            } else {
                adminRole = existingRoleOpt.get();
                boolean updated = false;
                if (!adminRoleName.equals(adminRole.getName())) {
                    adminRole.setName(adminRoleName);
                    updated = true;
                }
                if ((adminRoleDescription != null && !adminRoleDescription.equals(adminRole.getDescription()))
                        || (adminRoleDescription == null && adminRole.getDescription() != null)) {
                    adminRole.setDescription(adminRoleDescription);
                    updated = true;
                }
                if (updated) {
                    adminRole = roleRepository.save(adminRole);
                }
                log.info("Role {} already exists.", adminRoleCode);
            }

            ensureRoleHasAllPrivileges(adminRole);

            if (userRepository.findByUsername(adminUsername).isEmpty()) {
                var roles = new HashSet<Role>();
                roles.add(adminRole);

                Gender genderEnum = Gender.MALE;
                try {
                    genderEnum = Gender.valueOf(adminGender.toUpperCase());
                } catch (Exception ex) {
                    log.warn("Could not parse gender '{}' - using default MALE", adminGender);
                }

                LocalDate dob = LocalDate.of(1990, 1, 1);
                try {
                    dob = LocalDate.parse(adminDateOfBirth); // expecting yyyy-MM-dd
                } catch (Exception ex) {
                    log.warn("Could not parse dateOfBirth '{}' - using default 1990-01-01", adminDateOfBirth);
                }

                User user = User.builder()
                        .username(adminUsername)
                        .password(passwordEncoder.encode(adminPassword))
                        .roles(roles)
                        .email(adminEmail)
                        .address(adminAddress)
                        .firstName(adminFirstName)
                        .lastName(adminLastName)
                        .gender(genderEnum)
                        .phoneNumber(adminPhoneNumber)
                        .dateOfBirth(dob)
                        .isActive(true)
                        .isLocked(false)
                        .build();

                userRepository.save(user);
                log.info("Admin user '{}' has been created.", adminUsername);
            } else {
                log.info("Admin user '{}' already exists, skipping initialization.", adminUsername);
            }
        };
    }

    @SuppressWarnings("null")
    private void ensureRoleHasAllPrivileges(Role adminRole) {
        Set<Privilege> desired = EnumSet.allOf(Privilege.class);
        Set<Privilege> current = rolePrivilegeRepository.findByRole(adminRole).stream()
                .filter(rp -> Boolean.TRUE.equals(rp.getPermitted()))
                .map(RolePrivilege::getPrivilege)
                .collect(Collectors.toSet());
        if (current.containsAll(desired) && desired.containsAll(current)) {
            return;
        }
        rolePrivilegeRepository.deleteByRole(adminRole);
        desired.forEach(privilege -> rolePrivilegeRepository.save(
                RolePrivilege.builder()
                        .role(adminRole)
                        .privilege(privilege)
                        .permitted(true)
                        .build()));
        log.info("Role {} is assigned all privileges.", adminRoleCode);
    }
}