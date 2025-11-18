package com.healthcare.patient.service;

import com.healthcare.patient.client.IamAuthClient;
import com.healthcare.patient.iam.dto.IamAuthRequest;
import com.healthcare.patient.iam.dto.IamAuthResponse;
import com.healthcare.patient.iam.dto.IamAuthResponseData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class IamAuthService {

    // Tiêm interface client (đã sửa)
    private final IamAuthClient iamAuthClient;

    /**
     * Service trung gian gọi Feign Client để xác thực token.
     * @param jwt Token (lấy từ header)
     * @return Dữ liệu xác thực (UserId, Roles, Privileges)
     */
    public IamAuthResponseData getAuthenticationData(String jwt) {
        log.debug("Calling IAM service to validate token...");

        // 1. Chuẩn bị request body (dùng DTO mới)
        IamAuthRequest request = IamAuthRequest.builder()
                .token(jwt)
                .build();

        // 2. Gọi Feign Client
        IamAuthResponse response = iamAuthClient.validateToken(request);

        // 3. Xử lý response
        if (response == null || response.getError() != null || response.getData() == null) {
            String errorMsg = (response != null && response.getError() != null)
                    ? response.getError().getMessage()
                    : "Unknown authentication error";
            log.warn("Token validation failed: {}", errorMsg);
            throw new RuntimeException(errorMsg); // Ném lỗi để Filter bắt
        }

        log.info("Token validated successfully for user: {}", response.getData().getUsername());
        return response.getData();
    }
}