package com.example.IAM.repository;

import com.example.IAM.entity.PasswordState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PasswordStateRepository extends JpaRepository<PasswordState, UUID> {
}
