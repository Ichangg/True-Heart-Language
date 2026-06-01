package com.englishcenter.backend.service;

import com.englishcenter.backend.repository.UserRepository;
import com.englishcenter.backend.service.Impl.AuthServiceImpl;
import com.englishcenter.backend.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void testLogin() {
        // Placeholder for login test
    }
}
