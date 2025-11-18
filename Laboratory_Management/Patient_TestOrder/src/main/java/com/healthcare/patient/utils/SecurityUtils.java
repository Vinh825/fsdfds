package com.healthcare.patient.utils;

import com.healthcare.patient.iam.dto.IamAuthResponseData;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Optional<IamAuthResponseData> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof IamAuthResponseData) {
            return Optional.of((IamAuthResponseData) authentication.getPrincipal());
        }
        return Optional.empty();
    }

    public static IamAuthResponseData requireCurrentUser() {
        return getCurrentUser()
                .orElseThrow(() -> new AccessDeniedException("User is not authenticated"));
    }

    public static String currentUserId() {
        return requireCurrentUser().getUserId();
    }

    public static String currentUsername() {
        return requireCurrentUser().getUsername();
    }
}
