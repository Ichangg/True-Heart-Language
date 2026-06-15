package com.englishcenter.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
public class ZaloService {

    @Value("${zalo.access-token:}")
    private String accessToken;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "https://openapi.zalo.me/v3.0";

    public Map<String, Object> sendMessage(String phone, String content) {
        if (accessToken == null || accessToken.isBlank()) {
            log.warn("⚠️ Zalo access token chưa được cấu hình");
            return Map.of("success", false, "message", "Zalo chưa được cấu hình");
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("access_token", accessToken);
            Map<String, Object> body = Map.of("phone", phone, "template_data", Map.of("content", content));
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(BASE_URL + "/oa/message/zns", entity, Map.class);
            return Map.of("success", response.getBody() != null && Integer.valueOf(0).equals(response.getBody().get("error")));
        } catch (Exception e) {
            log.error("Zalo send error: {}", e.getMessage());
            return Map.of("success", false, "message", e.getMessage());
        }
    }

    public Map<String, Object> sendOAMessage(String userId, String content) {
        if (accessToken == null || accessToken.isBlank()) {
            return Map.of("success", false, "message", "Zalo chưa được cấu hình");
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("access_token", accessToken);
            Map<String, Object> body = Map.of("recipient", Map.of("user_id", userId), "message", Map.of("text", content));
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(BASE_URL + "/oa/message/cs", entity, Map.class);
            return Map.of("success", response.getBody() != null && Integer.valueOf(0).equals(response.getBody().get("error")));
        } catch (Exception e) {
            log.error("Zalo OA send error: {}", e.getMessage());
            return Map.of("success", false, "message", e.getMessage());
        }
    }
}
