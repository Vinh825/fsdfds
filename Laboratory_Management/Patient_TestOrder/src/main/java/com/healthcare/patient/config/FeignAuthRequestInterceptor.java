package com.healthcare.patient.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;


public class FeignAuthRequestInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public void apply(RequestTemplate template) {
        // Lấy request hiện tại (request 1: User -> Patient)
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();

            // Lấy header "Authorization" (Token A) từ request đó
            String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

            if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
                // Đính kèm header này vào request mới (request 2: Patient -> Identity)
                template.header(AUTHORIZATION_HEADER, authorizationHeader);
            }
        }
    }
}
