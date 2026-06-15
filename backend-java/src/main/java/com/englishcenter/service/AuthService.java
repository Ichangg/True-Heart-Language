package com.englishcenter.service;

import com.englishcenter.dto.auth.*;
import com.englishcenter.entity.User;
import com.englishcenter.enums.Role;
import com.englishcenter.enums.UserStatus;
import com.englishcenter.exception.BusinessException;
import com.englishcenter.exception.DuplicateResourceException;
import com.englishcenter.exception.ResourceNotFoundException;
import com.englishcenter.repository.UserRepository;
import com.englishcenter.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public User register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Tên đăng nhập đã tồn tại.");
        }

        Role role = Role.student;
        if (request.getRole() != null) {
            try { role = Role.valueOf(request.getRole()); } catch (IllegalArgumentException ignored) {}
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(role)
                .address(request.getAddress())
                .dateOfBirth(request.getDateOfBirth() != null ? LocalDate.parse(request.getDateOfBirth()) : null)
                .status(UserStatus.active)
                .build();

        return userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException("Tên đăng nhập hoặc mật khẩu không đúng.", 401));

        if (user.getStatus() == UserStatus.inactive) {
            throw new BusinessException("Tài khoản đã bị vô hiệu hóa.", 403);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("Tên đăng nhập hoặc mật khẩu không đúng.", 401);
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getUsername(), user.getRole().name());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        return AuthResponse.builder()
                .user(user)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String refreshToken(String token) {
        if (!jwtTokenProvider.validateRefreshToken(token)) {
            throw new BusinessException("Refresh token không hợp lệ hoặc đã hết hạn.", 401);
        }

        Long userId = jwtTokenProvider.getUserIdFromRefreshToken(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Refresh token không hợp lệ.", 401));

        if (user.getStatus() == UserStatus.inactive) {
            throw new BusinessException("Tài khoản đã bị vô hiệu hóa.", 401);
        }

        return jwtTokenProvider.generateAccessToken(user.getId(), user.getUsername(), user.getRole().name());
    }

    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại."));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BusinessException("Mật khẩu cũ không đúng.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
