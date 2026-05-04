package com.mumuwest.mumumike.utils;

import jakarta.servlet.http.HttpServletRequest;

public class AuthUtil {
    private AuthUtil() {
    }

    public static String getBearerToken(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ") || header.length() <= 7) {
            return null;
        }
        String token = header.substring(7);
        return JwtUtil.validateToken(token) ? token : null;
    }

    public static String getUsername(HttpServletRequest request) {
        String token = getBearerToken(request);
        if (token == null) {
            return null;
        }
        return JwtUtil.getUsernameFromToken(token);
    }

    public static Integer getRole(HttpServletRequest request) {
        String token = getBearerToken(request);
        if (token == null) {
            return null;
        }
        return JwtUtil.getRoleFromToken(token);
    }
}
