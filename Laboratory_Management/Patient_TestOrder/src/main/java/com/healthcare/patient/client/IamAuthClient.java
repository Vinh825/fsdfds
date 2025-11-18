package com.healthcare.patient.client;

import com.healthcare.patient.iam.config.IamFeignConfig;
import com.healthcare.patient.iam.dto.IamAuthRequest;
import com.healthcare.patient.iam.dto.IamAuthResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign Client Interface: Khai báo cách gọi sang IAM Service.
 */
@FeignClient(
        name = "iam-service", // Tên logic của service (cho Eureka)
        url = "${iam.service.url}", // URL trỏ đến IAM (từ application.properties)
        configuration = IamFeignConfig.class // Sử dụng config gộp ở trên
)
public interface IamAuthClient {

    /**
     * Gọi API /api/public/login bên IAM để validate JWT.
     * API này được bảo vệ bằng X-API-Key (đã được config tự động thêm vào).
     */
    @PostMapping("/api/public/login")
    IamAuthResponse validateToken(@RequestBody IamAuthRequest request);
}