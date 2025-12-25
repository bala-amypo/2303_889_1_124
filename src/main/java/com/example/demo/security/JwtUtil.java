package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    // ⚠️ Demo secret — OK for assignment/testing
    private static final String DEFAULT_SECRET =
            "demo-secret-key-demo-secret-key-demo-secret-key";

    private static final long DEFAULT_EXPIRATION = 3600000L; // 1 hour

    private final Key signingKey;
    private final long expirationTime;

    // ✅ REQUIRED BY YOUR TEST (byte[], long)
    public JwtUtil(byte[] secret, long expirationTime) {
        this.signingKey = Keys.hmacShaKeyFor(secret);
        this.expirationTime = expirationTime;
    }

    // ✅ REQUIRED BY SPRING (@Component)
    public JwtUtil() {
        this.signingKey = Keys.hmacShaKeyFor(DEFAULT_SECRET.getBytes());
        this.expirationTime = DEFAULT_EXPIRATION;
    }

    // ================= TOKEN GENERATION =================

    public String generateToken(Long userId, String email, String role) {
        return Jwts.builder()
                .setClaims(Map.of(
                        "userId", userId,
                        "role", role
                ))
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // ================= TOKEN EXTRACTION =================

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Long extractUserId(String token) {
        return extractAllClaims(token).get("userId", Long.class);
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // ================= VALIDATION =================

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // ================= INTERNAL =================

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
