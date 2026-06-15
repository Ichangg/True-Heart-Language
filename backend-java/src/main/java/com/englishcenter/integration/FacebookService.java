package com.englishcenter.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
public class FacebookService {

    @Value("${facebook.page-access-token:}")
    private String pageAccessToken;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "https://graph.facebook.com/v18.0";

    public Map<String, Object> sendMessage(String recipientId, String content) {
        if (pageAccessToken == null || pageAccessToken.isBlank()) {
            log.warn("⚠️ Facebook page access token chưa được cấu hình");
            return Map.of("success", false, "message", "Facebook chưa được cấu hình");
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, Object> body = Map.of(
                    "recipient", Map.of("id", recipientId),
                    "message", Map.of("text", content),
                    "messaging_type", "MESSAGE_TAG",
                    "tag", "ACCOUNT_UPDATE"
            );
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            String url = BASE_URL + "/me/messages?access_token=" + pageAccessToken;
            restTemplate.postForEntity(url, entity, Map.class);
            return Map.of("success", true);
        } catch (Exception e) {
            log.error("Facebook send error: {}", e.getMessage());
            return Map.of("success", false, "message", e.getMessage());
        }
    }
}
