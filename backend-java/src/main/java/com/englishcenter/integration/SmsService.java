package com.englishcenter.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
public class SmsService {

    @Value("${sms.api-url:}")
    private String apiUrl;

    @Value("${sms.api-key:}")
    private String apiKey;

    @Value("${sms.brand-name:EnglishCenter}")
    private String brandName;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> sendSms(String phone, String content) {
        if (apiUrl == null || apiUrl.isBlank() || apiKey == null || apiKey.isBlank()) {
            log.warn("⚠️ SMS gateway chưa được cấu hình");
            return Map.of("success", false, "message", "SMS gateway chưa được cấu hình");
        }
        try {
            Map<String, Object> body = Map.of("to", phone, "content", content, "brand_name", brandName, "api_key", apiKey);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body);
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);
            boolean success = response.getBody() != null && "success".equals(response.getBody().get("status"));
            return Map.of("success", success);
        } catch (Exception e) {
            log.error("SMS send error: {}", e.getMessage());
            return Map.of("success", false, "message", e.getMessage());
        }
    }
}
