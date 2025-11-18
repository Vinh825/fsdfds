package com.healthcare.patient; // Phải là package chính của bạn

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE) // Đảm bảo Filter này chạy ĐẦU TIÊN
public class CorsGlobalFilter {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);

        // Dùng 'setAllowedOriginPatterns' để cho phép 517* (5174, 5175, v.v.)
        config.setAllowedOriginPatterns(List.of("http://localhost:517*"));

        // Các header mà FE của bạn sử dụng (Thêm X-User-Id)
        config.setAllowedHeaders(Arrays.asList(
                "Origin",    "Content-Type", "Accept", "Authorization", "X-User-Id", "X-Requested-With"
        ));

        // Cho phép TẤT CẢ các phương thức (GET, POST, PUT, DELETE, OPTIONS)
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Áp dụng cấu hình này cho TẤT CẢ các đường dẫn
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}