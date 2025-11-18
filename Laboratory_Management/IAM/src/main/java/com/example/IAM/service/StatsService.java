package com.example.IAM.service;

import com.example.IAM.dto.respone.DashboardStatsResponse;
import com.example.IAM.repository.RoleRepository;
import com.example.IAM.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatsService {

    UserRepository userRepository;
    RoleRepository roleRepository;

    public DashboardStatsResponse getDashboardStats() {
        long totalUsers = userRepository.countByDeletedFalse();
        long activeUsers = userRepository.countByDeletedFalseAndIsActiveTrue();
        long totalRoles = roleRepository.countByDeletedAtIsNull();
        return new DashboardStatsResponse(totalUsers, activeUsers, totalRoles);
    }
}
