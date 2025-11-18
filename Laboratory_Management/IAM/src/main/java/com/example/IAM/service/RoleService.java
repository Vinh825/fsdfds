package com.example.IAM.service;

import com.example.IAM.common.exception.AppException;
import com.example.IAM.common.exception.ErrorCode;
import com.example.IAM.dto.request.*;
import com.example.IAM.dto.respone.*;
import com.example.IAM.dto.respone.RoleResponse.PrivilegeGrant;
import com.example.IAM.entity.*;
import com.example.IAM.repository.*;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleService {

    final RoleRepository roleRepo;
    final RolePrivilegeRepository rolePrivRepo;

    @Autowired
    EntityManager entityManager;

    @Value("${info.admin.role-code:}")
    String adminRoleCode;

    static final Privilege DEFAULT_PRIV = Privilege.READ_ONLY;

    @Transactional
    public RoleResponse create(CreateRoleRequest req) {
        if (roleRepo.existsByRoleCodeIgnoreCase(req.roleCode())) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }

        Role role = Objects.requireNonNull(Role.builder()
                .name(req.name().trim())
                .createdBy(req.roleCode())
                .roleCode(req.roleCode().trim().toUpperCase(Locale.ROOT))
                .description(req.description())
                .build());
        roleRepo.save(role);

        var grants = normalizeGrants(req.privileges());
        applyGrantsReplace(role, grants);

        return toResponse(role);
    }

    @Transactional
    public RoleResponse update(String roleCode, UpdateRoleRequest req) {
        Role role = roleRepo.findByRoleCodeAndDeletedAtIsNull(roleCode)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        if (role.isDeleted()) {
            throw new AppException(ErrorCode.ROLE_DELETED);
        }

        role.setName(req.name().trim());
        role.setDescription(req.description());
        role.setUpdatedAt(OffsetDateTime.now());
        role.setUpdatedBy(req.name());
        roleRepo.save(role);

        var grants = normalizeGrants(req.privileges());
        applyGrantsReplace(role, grants);

        return toResponse(role);
    }

    @Transactional
    public void delete(String roleCode) {
        Role role = roleRepo.findByRoleCodeAndDeletedAtIsNull(roleCode)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        if (role.isDeleted()) {
            throw new AppException(ErrorCode.ROLE_DELETED);
        }

        if (roleRepo.isRoleInUse(role.getId())) {
            throw new AppException(ErrorCode.ROLE_IN_USE);
        }

        role.setDeletedAt(OffsetDateTime.now());
        role.setUpdatedAt(OffsetDateTime.now());
        roleRepo.save(role);
    }

    public List<RoleResponse> getAllRoles() {
        return roleRepo.findAll().stream()
                .filter(role -> !role.isDeleted())
                .filter(role -> !isAdminRole(role))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public RoleResponse getRoleByRoleCode(String roleCode) {
        Role role = roleRepo.findByRoleCodeAndDeletedAtIsNull(roleCode)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        if (role.isDeleted()) {
            throw new AppException(ErrorCode.ROLE_DELETED);
        }
        return toResponse(role);
    }

    @Transactional
    public PageResponse<RoleResponse> list(int page, int size, String q, String privilege, Sort sort) {
        Sort safeSort = (sort == null) ? Sort.by("name").ascending() : sort;
        Pageable pageable = PageRequest.of(page, size, safeSort);
        Privilege privilegeFilter = null;
        if (privilege != null && !privilege.isBlank()) {
            try {
                privilegeFilter = Privilege.valueOf(privilege.trim().toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ex) {
                throw new AppException(ErrorCode.INVALID_KEY);
            }
        }

        Privilege finalPrivilegeFilter = privilegeFilter;
        Page<Role> p = roleRepo.findAll((root, query, cb) -> {
            var preds = new ArrayList<jakarta.persistence.criteria.Predicate>();
            preds.add(cb.isNull(root.get("deletedAt")));
            if (q != null && !q.isBlank()) {
                String like = "%" + q.toLowerCase(Locale.ROOT) + "%";
                preds.add(cb.or(
                        cb.like(cb.lower(root.get("name")), like),
                        cb.like(cb.lower(root.get("roleCode")), like),
                        cb.like(cb.lower(root.get("description")), like)));
            }
            if (finalPrivilegeFilter != null) {
                var privJoin = root.join("rolePrivileges", jakarta.persistence.criteria.JoinType.LEFT);
                preds.add(cb.and(
                        cb.equal(privJoin.get("privilege"), finalPrivilegeFilter),
                        cb.isTrue(privJoin.get("permitted"))));
                if (query != null) {
                    query.distinct(true);
                }
            }

            String normalizedAdminRoleCode = getNormalizedAdminRoleCode();
            if (normalizedAdminRoleCode != null) {
                preds.add(cb.notEqual(cb.lower(root.get("roleCode")), normalizedAdminRoleCode));
            }
            return cb.and(preds.toArray(jakarta.persistence.criteria.Predicate[]::new));
        }, pageable);

        var items = p.getContent().stream().map(this::toResponse).toList();
        return new PageResponse<>(items, p.getNumber(), p.getSize(), p.getTotalElements());
    }


    private List<PrivilegeGrant> normalizeGrants(Set<PrivilegeEntry> entries) {
        if (entries == null || entries.isEmpty()) {
            return List.of(new PrivilegeGrant(DEFAULT_PRIV, true));
        }
        Map<Privilege, Boolean> map = new LinkedHashMap<>();
        for (PrivilegeEntry e : entries) {
            if (e == null || e.privilege() == null)
                continue;
            map.put(e.privilege(), e.permitted() == null ? Boolean.TRUE : e.permitted());
        }
        if (map.isEmpty())
            map.put(DEFAULT_PRIV, true);
        return map.entrySet().stream()
                .map(it -> new PrivilegeGrant(it.getKey(), it.getValue()))
                .toList();
    }

    /**
     * Replace semantics: xoá hết grant cũ, tạo lại theo danh sách mới (1
     * dòng/privilege)
     */
    private void applyGrantsReplace(Role role, List<PrivilegeGrant> grants) {
        rolePrivRepo.deleteByRole(role);
        entityManager.flush();
        for (PrivilegeGrant g : grants) {
            RolePrivilege entry = RolePrivilege.builder()
                    .role(role)
                    .privilege(g.privilege())
                    .permitted(Boolean.TRUE.equals(g.permitted()))
                    .build();
            rolePrivRepo.save(Objects.requireNonNull(entry));
        }
    }

    private RoleResponse toResponse(Role role) {
        List<PrivilegeGrant> grants = rolePrivRepo.findByRole(role).stream()
                .sorted(Comparator.comparing(rp -> rp.getPrivilege().name()))
                .map(rp -> new PrivilegeGrant(rp.getPrivilege(), Boolean.TRUE.equals(rp.getPermitted())))
                .collect(Collectors.toList());
        return new RoleResponse(
                role.getRoleCode(), role.getName(), role.getDescription(),
                grants, role.getCreatedAt(), role.getUpdatedAt());
    }

    private String getNormalizedAdminRoleCode() {
        if (!StringUtils.hasText(adminRoleCode)) {
            return null;
        }
        return adminRoleCode.trim().toLowerCase(Locale.ROOT);
    }

    private boolean isAdminRole(Role role) {
        if (role == null || !StringUtils.hasText(role.getRoleCode())) {
            return false;
        }
        if (!StringUtils.hasText(adminRoleCode)) {
            return false;
        }
        return role.getRoleCode().trim().equalsIgnoreCase(adminRoleCode.trim());
    }
}