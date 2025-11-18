package com.example.IAM.controller;

import com.example.IAM.dto.respone.ApiResponse;
import com.example.IAM.dto.respone.DashboardStatsResponse;
import com.example.IAM.service.StatsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatsController {

    StatsService statsService;

    @GetMapping("/dashboard")
    public ApiResponse<DashboardStatsResponse> getDashboardStats() {
        var result = statsService.getDashboardStats();
        return ApiResponse.<DashboardStatsResponse>builder().result(result).build();
    }
}
