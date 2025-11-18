package com.healthcare.patient.iam.config;

import feign.RequestInterceptor;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * Cấu hình tập trung cho Feign Client gọi sang IAM Service.
 * Bao gồm Interceptor (thêm API Key) và ErrorDecoder (xử lý lỗi 401).
 */
@Configuration
public class IamFeignConfig {

    @Value("${app.api-key.name}")
    private String apiKeyHeaderName;

    @Value("${app.api-key.value}")
    private String apiKeyValue;

    /**
     * Bean Interceptor: Tự động thêm X-API-Key vào header mỗi request.
     */
    @Bean
    public RequestInterceptor apiKeyRequestInterceptor() {
        return template -> {
            template.header(apiKeyHeaderName, apiKeyValue);
        };
    }

    /**
     * Bean ErrorDecoder: Xử lý lỗi trả về từ IAM.
     */
    @Bean
    public ErrorDecoder feignErrorDecoder() {
        return new IamErrorDecoder();
    }

    class IamErrorDecoder implements ErrorDecoder {

        private final ErrorDecoder defaultDecoder = new Default();

        @Override
        public Exception decode(String methodKey, Response response) {
            HttpStatus status = HttpStatus.valueOf(response.status());

            // Nếu IAM service trả về 401 (Token sai, hết hạn) hoặc 403 (Không có quyền)
            if (status.equals(HttpStatus.UNAUTHORIZED) || status.equals(HttpStatus.FORBIDDEN)) {
                // Ném exception này để GlobalExceptionHandler bắt được
                return new RuntimeException("IAM Authentication failed: " + status.getReasonPhrase());
            }

            // Các lỗi khác (500, 404...) thì trả về lỗi mặc định
            return defaultDecoder.decode(methodKey, response);
        }
    }
}
