
package com.example.IAM.service;

import com.example.IAM.common.exception.AppException;
import com.example.IAM.common.exception.ErrorCode;
import com.example.IAM.common.mapper.UserMapper;
import com.example.IAM.dto.request.ChangcePasswordRequest;
import com.example.IAM.dto.request.CreateUserRequest;
import com.example.IAM.dto.request.UpdateUserRequest;
import com.example.IAM.dto.respone.ChangcePasswordRespone;
import com.example.IAM.dto.respone.PageResponse;
import com.example.IAM.dto.respone.UserResponse;
import com.example.IAM.entity.Gender;
import com.example.IAM.entity.PasswordState;
import com.example.IAM.entity.Role;
import com.example.IAM.entity.User;
import com.example.IAM.repository.RoleRepository;
import com.example.IAM.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import java.text.Normalizer;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.stream.LangCollectors.collect;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class UserService {
    final UserMapper userMapper;
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final MailCreateAccount mailCreateAccount;
    final RoleRepository roleRepository;

    @Value("${info.admin.username:}")
    String adminUsername;

    static final int PASSWORD_MAX_AGE_DAYS = 90;

    @Transactional
    public ChangcePasswordRespone changePassword(UUID id, ChangcePasswordRequest request) {
        Objects.requireNonNull(id, "User id must not be null");

        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (Boolean.TRUE.equals(user.getIsLocked()))
            throw new AppException(ErrorCode.USER_LOCKED);
        if (!Boolean.TRUE.equals(user.getIsActive()))
            throw new AppException(ErrorCode.USER_INACTIVE);
        if (user.getDeleted())
            throw new AppException(ErrorCode.USER_DELETED);

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword()))
            throw new AppException(ErrorCode.PASSSWORD_OLD_INCORRECT);

        if (!request.getNewPassword().equals(request.getConfirmNewPassword()))
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword()))
            throw new AppException(ErrorCode.PASSWORD_SAME_AS_OLD);

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        OffsetDateTime now = OffsetDateTime.now();
        PasswordState state = user.getPasswordState();
        if (state == null) {
            state = PasswordState.builder()
                    .user(user)
                    .passwordChangedAt(now)
                    .passwordExpiryDate(now.plusDays(PASSWORD_MAX_AGE_DAYS))
                    .build();
        } else {
            state.setPasswordChangedAt(now);
            state.setPasswordExpiryDate(now.plusDays(PASSWORD_MAX_AGE_DAYS));
        }

        user.setPasswordState(state);
        user.setMustChangePassword(false);
        userRepository.save(user);

        return ChangcePasswordRespone.builder().changed(true).build();
    }

    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        if (userRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new AppException(ErrorCode.PHONE_NUMBER_EXISTED);
        }

        String username = generateUniqueUsername(request.getLastName(), request.getFirstName());
        String password = generateRandomPassword(12);

        OffsetDateTime now = OffsetDateTime.now();
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(password));
        user.setUsername(username);
        user.setFullName(request.getLastName() + " " + request.getFirstName());
        user.setIsActive(true);
        user.setIsLocked(false);
        user.setFailedLoginAttempts(0);
        user.setMustChangePassword(true);
        user.setCreatedAt(now);

        PasswordState state = PasswordState.builder()
                .user(user)
                .passwordChangedAt(now)
                .passwordExpiryDate(now.plusDays(PASSWORD_MAX_AGE_DAYS))
                .build();
        user.setPasswordState(state);
        Set<Role> assignedRoles = resolveRoles(request.getRoleCodes());
        user.setRoles(new HashSet<>(assignedRoles));

        User savedUser = userRepository.save(user);

        mailCreateAccount.sendEmailCreateAccount(savedUser.getEmail(), savedUser.getFullName(), username, password);

        return userMapper.toUserResponse(savedUser);
    }

    public PageResponse<UserResponse> list(int page, int size, String q, Gender gender, String roleCode) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100);
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        Specification<User> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.or(cb.isNull(root.get("deleted")), cb.isFalse(root.get("deleted"))));
            predicates.add(cb.isNull(root.get("deletedAt")));

            if (StringUtils.hasText(q)) {
                String likeValue = "%" + q.trim().toLowerCase(Locale.ROOT) + "%";
                Predicate searchPredicate = cb.or(
                        cb.like(cb.lower(root.get("email")), likeValue),
                        cb.like(cb.lower(root.get("firstName")), likeValue),
                        cb.like(cb.lower(root.get("lastName")), likeValue),
                        cb.like(cb.lower(root.get("phoneNumber")), likeValue),
                        cb.like(cb.lower(root.get("identityNumber")), likeValue));
                predicates.add(searchPredicate);
            }

            if (gender != null) {
                predicates.add(cb.equal(root.get("gender"), gender));
            }

            if (StringUtils.hasText(roleCode)) {
                Join<User, ?> roleJoin = root.join("roles", JoinType.LEFT);
                predicates.add(cb.equal(cb.lower(roleJoin.get("roleCode")), roleCode.trim().toLowerCase(Locale.ROOT)));
                if (query != null) {
                    query.distinct(true);
                }
            }

            String normalizedAdminUsername = getNormalizedAdminUsername();
            if (normalizedAdminUsername != null) {
                predicates.add(cb.notEqual(cb.lower(root.get("username")), normalizedAdminUsername));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };

        Page<User> usersPage = userRepository.findAll(spec, pageable);
        var items = usersPage.getContent().stream()
                .map(userMapper::toUserResponse)
                .toList();
        return new PageResponse<>(items, usersPage.getNumber(), usersPage.getSize(), usersPage.getTotalElements());
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .filter(user -> !user.getDeleted())
                .filter(user -> !isAdminUser(user))
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(UUID id) {
        Objects.requireNonNull(id, "User id must not be null");
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (user.getDeleted())
            throw new AppException(ErrorCode.USER_DELETED);
        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(UUID id, @Valid UpdateUserRequest request) {
        User user = findMutableUser(id);

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }
        if (request.getDateOfBirth() != null) {
            user.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }

        user.setUpdatedAt(OffsetDateTime.now());

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(UUID id) {
        User user = findMutableUser(id);

        user.setDeleted(true);
        user.setDeletedAt(OffsetDateTime.now());
        user.setIsActive(false);
        user.setUpdatedAt(OffsetDateTime.now());
        user.setIsLocked(true);
        userRepository.saveAndFlush(user);
    }

    @Transactional
    public UserResponse updateUserRoles(UUID id, Set<String> roleCodes) {
        User user = findMutableUser(id);
        Set<Role> roles = resolveRoles(roleCodes);
        user.setRoles(new HashSet<>(roles));
        user.setUpdatedAt(OffsetDateTime.now());
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse updateUserLock(UUID id, boolean locked) {
        User user = findMutableUser(id);
        user.setIsLocked(locked);
        if (locked) {
            user.setIsActive(false);
        } else if (!user.getDeleted()) {
            user.setIsActive(true);
        }
        user.setUpdatedAt(OffsetDateTime.now());
        return userMapper.toUserResponse(userRepository.save(user));
    }

private String removeUnicode(String input) {
    if (input == null) return "";
    return Normalizer.normalize(input, Normalizer.Form.NFD)
            .replaceAll("\\p{M}", "")
            .replaceAll("[^a-zA-Z0-9\\s]", "")
            .replaceAll("\\s+", "")
            .trim();
}

    private String getInitials(String name) {
        if (name == null || name.isBlank()) return "";
        return Stream.of(name.split("\\s+"))
                .filter(s -> !s.isEmpty())
                .map(s -> removeUnicode(s.substring(0, 1)))
                .collect(Collectors.joining())
                .toUpperCase();
    }

    private String generateUniqueUsername(String firstName, String lastName) {
        String cleanFirst = removeUnicode(firstName);
        String initialsLast = getInitials(lastName);

        String baseUsername =  cleanFirst + initialsLast + "HL";

        String finalUsername = baseUsername;
        int counter = 1;

        while (userRepository.findByUsername(finalUsername).isPresent()) {
            finalUsername = baseUsername + "_" + counter;
            counter++;
        }

        return finalUsername;
    }

    private String generateRandomPassword(int length) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, length);
    }

    private String getNormalizedAdminUsername() {
        if (!StringUtils.hasText(adminUsername)) {
            return null;
        }
        return adminUsername.trim().toLowerCase(Locale.ROOT);
    }

    private boolean isAdminUser(User user) {
        if (user == null || user.getUsername() == null) {
            return false;
        }
        if (!StringUtils.hasText(adminUsername)) {
            return false;
        }
        return user.getUsername().trim().equalsIgnoreCase(adminUsername.trim());
    }

    private User findMutableUser(UUID id) {
        Objects.requireNonNull(id, "User id must not be null");
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (user.getDeleted()) {
            throw new AppException(ErrorCode.USER_DELETED);
        }
        if (isAdminUser(user)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        return user;
    }

    private Set<Role> resolveRoles(Set<String> roleCodes) {
        if (roleCodes == null || roleCodes.isEmpty()) {
            return Collections.emptySet();
        }

        Set<String> normalizedCodes = roleCodes.stream()
                .filter(StringUtils::hasText)
                .map(code -> code.trim().toUpperCase(Locale.ROOT))
                .collect(Collectors.toSet());

        if (normalizedCodes.isEmpty()) {
            return Collections.emptySet();
        }

        List<Role> roles = roleRepository.findByRoleCodeInAndDeletedAtIsNull(normalizedCodes);
        Set<String> foundCodes = roles.stream()
                .map(Role::getRoleCode)
                .map(code -> code == null ? null : code.trim().toUpperCase(Locale.ROOT))
                .collect(Collectors.toSet());

        if (!foundCodes.containsAll(normalizedCodes)) {
            throw new AppException(ErrorCode.ROLE_NOT_EXISTED);
        }

        return new HashSet<>(roles);
    }
}
