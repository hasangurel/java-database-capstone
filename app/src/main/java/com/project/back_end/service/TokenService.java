package com.project.back_end.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for JWT token generation and validation.
 * Handles authentication tokens for admin, doctor, and patient roles.
 */
@Service
public class TokenService {

    private static final String SECRET_KEY = "MySecretKeyForJWTTokenGenerationAndValidation12345678901234567890";
    private static final long EXPIRATION_TIME = 86400000; // 24 hours in milliseconds

    private final SecretKey key;

    public TokenService() {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /**
     * Generate JWT token for a user.
     *
     * @param userId the user ID
     * @param username the username
     * @param role the user role (ADMIN, DOCTOR, PATIENT)
     * @return JWT token string
     */
    public String generateToken(Long userId, String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validate JWT token.
     *
     * @param token the JWT token
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extract claims from JWT token.
     *
     * @param token the JWT token
     * @return Claims object containing token data
     */
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extract username from token.
     *
     * @param token the JWT token
     * @return username
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * Extract role from token.
     *
     * @param token the JWT token
     * @return role
     */
    public String extractRole(String token) {
        return (String) extractClaims(token).get("role");
    }

    /**
     * Extract user ID from token.
     *
     * @param token the JWT token
     * @return user ID
     */
    public Long extractUserId(String token) {
        Object userId = extractClaims(token).get("userId");
        if (userId instanceof Integer) {
            return ((Integer) userId).longValue();
        }
        return (Long) userId;
    }

    /**
     * Check if token is expired.
     *
     * @param token the JWT token
     * @return true if expired, false otherwise
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = extractClaims(token).getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Validate token and check if it has the required role.
     *
     * @param token the JWT token
     * @param requiredRole the required role
     * @return true if valid and has required role
     */
    public boolean validateTokenAndRole(String token, String requiredRole) {
        if (!validateToken(token) || isTokenExpired(token)) {
            return false;
        }
        String role = extractRole(token);
        return requiredRole.equalsIgnoreCase(role);
    }
}
