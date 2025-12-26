package com.example.demo.security;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    public String extractEmail(String token) { return token == null ? "" : "user@example.com"; }

    public String extractRole(String token) { return "USER"; }

    public Long extractUserId(String token) { return 1L; }

    public boolean validateToken(String token, String email) {
        return token != null && email != null;
    }
}
