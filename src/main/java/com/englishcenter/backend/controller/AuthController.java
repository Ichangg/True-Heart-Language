package com.englishcenter.backend.controller;

import com.englishcenter.backend.dto.RegisterRequest;
import com.englishcenter.backend.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // Corrected import
import com.englishcenter.backend.service.AuthService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired 
    private AuthService authService;

    // ==========================================
    // API ĐĂNG NHẬP
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

  
    // API ĐĂNG KÝ (REGISTER)
   
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    // ==========================================
    // API TEST TẠO MẬT KHẨU MÃ HÓA
    
    @GetMapping("/generate-hash")
    public ResponseEntity<Map<String, String>> generateHash(@RequestParam(defaultValue = "123456") String rawPassword) {
        return authService.generateHash(rawPassword);
    }
}