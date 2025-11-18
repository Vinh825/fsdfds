package com.healthcare.patient.controller;

import com.healthcare.patient.iam.dto.IamAuthResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/test-auth")
public class FeignTestController {

    /**
     * API này dùng để test xem SecurityContext có hoạt động không.
     * Gửi request đến API này với header "Authorization: Bearer <token>"
     * Nếu token hợp lệ, nó sẽ trả về thông tin user lấy từ IAM.
     */
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo() {
        // Lấy thông tin đã được JwtAuthenticationFilter set
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getDetails() == null) {
            return ResponseEntity.status(401).body("No valid authentication found.");
        }

        // Lấy object AuthenticationResponseData đã lưu trong details
        IamAuthResponseData authData = (IamAuthResponseData) authentication.getDetails();
        return ResponseEntity.ok(authData);
    }

    /**
     * API này test phân quyền dựa trên Privilege.
     * Chỉ ai có quyền "ADMIN" (code) mới gọi được.
     */
    @GetMapping("/admin-only")
    @PreAuthorize("hasAnyAuthority('ADMIN_ACCESS')")
    public ResponseEntity<String> getAdminData() {
        return ResponseEntity.ok("Welcome Admin. You have the 'ADMIN' privilege.");
    }
    @GetMapping("/admin-only.")
    @PreAuthorize("hasAnyAuthority('READ_PATIENT')")
    public ResponseEntity<String> getAdminDatat() {
        return ResponseEntity.ok("Welcome Admin. You have the 'ADMIN' privilege.");
    }
}
