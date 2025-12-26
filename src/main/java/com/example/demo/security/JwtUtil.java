package com.example.demo.security;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    // Required by AuthController
    public String generateToken(String email) {
        return "dummy-token";
    }

    // Required by tests
    public String extractEmail(String token) {
        return "";
    }

    public String extractRole(String token) {
        return "";
    }

    public Long extractUserId(String token) {
        return null;
    }

    public boolean validateToken(String token, String email) {
        return false;
    }
}
