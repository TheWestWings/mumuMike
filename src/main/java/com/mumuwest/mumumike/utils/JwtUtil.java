package com.mumuwest.mumumike.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY = "your-secret-key-123456789012345678901234567890129999999999999999999999999999"; // 至少 32 字节
    private static final long EXPIRATION_TIME = 964_000_000; // 10天

    public static String generateToken(String username, Integer role) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role) // 添加角色信息
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), Jwts.SIG.HS512) // 使用新 API
                .compact();
    }

    public static String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public static Integer getRoleFromToken(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", Integer.class);
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}