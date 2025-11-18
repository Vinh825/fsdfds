package com.healthcare.patient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Áp dụng cho tất cả các đường dẫn bắt đầu bằng /api/
                registry.addMapping("/api/**")
                        // Dùng 'allowedOriginPatterns' để cho phép 517* (5174, 5175, v.v.)
                        .allowedOriginPatterns("http://localhost:517*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")

                        // Cho phép tất cả các header
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
