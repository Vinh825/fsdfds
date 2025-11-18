package com.example.IAM.repository;

import com.example.IAM.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RolePrivilegeRepository extends JpaRepository<RolePrivilege, UUID> {
    List<RolePrivilege> findByRole(Role role);

    @Modifying
    @Transactional
    @Query("DELETE FROM RolePrivilege rp WHERE rp.role = :role")
    void deleteByRole(@Param("role") Role role);

    boolean existsByRoleAndPrivilege(Role role, Privilege privilege);
}