package com.englishcenter.backend.service;

import com.englishcenter.backend.dto.RegisterRequest;
import com.englishcenter.backend.dto.LoginRequest;
import com.englishcenter.backend.dto.LoginResponse;

import org.springframework.http.ResponseEntity;
import java.util.Map;

public interface AuthService {
    ResponseEntity<LoginResponse> login(LoginRequest request);
    ResponseEntity<Map<String, String>> register(RegisterRequest request);
    ResponseEntity<Map<String, String>> generateHash(String rawPassword);
}