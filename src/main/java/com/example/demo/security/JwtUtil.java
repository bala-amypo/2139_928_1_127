package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Must be long enough (HS256 requirement)
    private static final String SECRET =
            "mysecretkeymysecretkeymysecretkey123456";

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // Used by AuthController & tests
    public String generateToken(Long userId, String email, String role) {

        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                // ✅ IMPORTANT: ROLE_ prefix REQUIRED
                .claim("role", "ROLE_" + role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Backward compatibility (if controller calls this)
    public String generateToken(String email) {
        return generateToken(1L, email, "USER");
    }

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    public String extractRole(String token) {
        Object role = getClaims(token).get("role");
        return role == null ? null : role.toString();
    }

    public Long extractUserId(String token) {
        Object id = getClaims(token).get("userId");
        return id == null ? null : Long.valueOf(id.toString());
    }

    // ✅ Test expects validation against email
    public boolean validateToken(String token, String email) {
        return extractEmail(token).equals(email);
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
