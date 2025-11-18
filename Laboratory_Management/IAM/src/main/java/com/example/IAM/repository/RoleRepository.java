package com.example.IAM.repository;

import com.example.IAM.entity.Role;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID>, JpaSpecificationExecutor<Role> {
    Optional<Role> findByName(String roleName);

    Optional<Role> findByRoleCodeAndDeletedAtIsNull(String roleCode);

    boolean existsByRoleCodeIgnoreCase(String roleCode);

    @Query("SELECT COUNT(u) > 0 FROM User u JOIN u.roles r WHERE r.id = :roleId AND u.deletedAt IS NULL")
    boolean isRoleInUse(@Param("roleId") UUID roleId);

    long countByDeletedAtIsNull();

    List<Role> findByRoleCodeInAndDeletedAtIsNull(Collection<String> roleCodes);
}
