package com.englishcenter.backend.modules.common.service;

import com.englishcenter.backend.modules.common.dto.RegisterRequest;
import com.englishcenter.backend.modules.common.dto.LoginRequest;
import com.englishcenter.backend.modules.common.dto.LoginResponse;

import org.springframework.http.ResponseEntity;
import java.util.Map;

public interface AuthService {
    ResponseEntity<LoginResponse> login(LoginRequest request);
    ResponseEntity<Map<String, String>> register(RegisterRequest request);
    ResponseEntity<Map<String, String>> generateHash(String rawPassword);
}