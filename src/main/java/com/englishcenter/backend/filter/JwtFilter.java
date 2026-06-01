package com.englishcenter.backend.filter;

import com.englishcenter.backend.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain)
            throws ServletException, IOException {

        // Skip JWT validation for public endpoints
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/api/auth/")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String authHeader = request.getHeader("Authorization");
            System.out.println("=== JWT FILTER ===");
            System.out.println("URL: " + request.getRequestURI());
            System.out.println("Auth Header: " + authHeader);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                System.out.println("Token 20 ký tự đầu: " + token.substring(0, 20) + "...");

                boolean valid = jwtUtil.validateToken(token);
                System.out.println("Token valid: " + valid);

                if (valid) {
                    String email = jwtUtil.getEmailFromToken(token);
                    String role  = jwtUtil.getRoleFromToken(token);
                    System.out.println("Email: " + email);
                    System.out.println("Role: " + role);

                    var auth = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println("Auth set thành công!");
                }
            } else {
                System.out.println("Không có Authorization header!");
            }
        } catch (Exception e) {
            System.out.println("JWT Filter error: " + e.getMessage());
            SecurityContextHolder.clearContext();
        }
        System.out.println("==================");

        chain.doFilter(request, response);
    }
}