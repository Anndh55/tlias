package com.example.hello.utils;

import com.example.hello.entity.Emp;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtils {

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("zxyf-login-tlias-secret-key-2024-2025-springboot".getBytes());
    private static final long EXPIRATION = 12 * 60 * 60 * 1000L; // 12 hours

    public static String generateToken(Emp emp) {
        Date now = new Date();
        return Jwts.builder()
                .claim("id", emp.getId())
                .claim("username", emp.getUsername())
                .claim("name", emp.getName())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + EXPIRATION))
                .signWith(SECRET_KEY)
                .compact();
    }

    public static Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public static boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
