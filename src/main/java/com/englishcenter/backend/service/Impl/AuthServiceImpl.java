package com.englishcenter.backend.service.Impl;

import com.englishcenter.backend.dto.LoginRequest;
import com.englishcenter.backend.dto.LoginResponse;
import com.englishcenter.backend.dto.RegisterRequest;
import com.englishcenter.backend.entity.User;
import com.englishcenter.backend.repository.UserRepository;
import com.englishcenter.backend.service.AuthService;
import com.englishcenter.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override // Login method that authenticates the user and generates a JWT token
    public ResponseEntity<LoginResponse> login(LoginRequest request) { // tìm user theo email, kiểm tra password, nếu đúng thì tạo token và trả về thông tin user
        return userRepository.findByEmail(request.getEmail())
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) // Kiểm tra mật khẩu
                .map(user -> {
                    String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole()); // Tạo token JWT
                    return ResponseEntity.ok(new LoginResponse(token, user.getId(), user.getFullname(), user.getEmail(), user.getRole())); // Trả về token và thông tin user
                })
                .orElse(ResponseEntity.status(401).body(null)); // Trả về lỗi 401 Unauthorized nếu email hoặc password không đúng
    }

    @Override
    public ResponseEntity<Map<String, String>> register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
        }
        User user = new User();
        user.setFullname(request.getFullname());
        user.setRole(request.getRole() != null ? request.getRole() : "STUDENT");
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    @Override
    public ResponseEntity<Map<String, String>> generateHash(String rawPassword) {
        String hash = passwordEncoder.encode(rawPassword);
        return ResponseEntity.ok(Map.of("password", rawPassword, "hash", hash));
    }
}
