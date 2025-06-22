package com.example.kptech.quickserv.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final Key key;
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours

    // Inject secret key from application.properties
    public JwtService(@Value("${jwt.secret}") String secretKey) {
        if (secretKey.length() < 32) {
            throw new IllegalArgumentException("JWT secret key must be at least 256 bits (32 characters).");
        }
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // Generate JWT token for email
    public String generateToken(String emailId) {
        return Jwts.builder()
                .setSubject(emailId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract username/email from token
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Check if token is valid for given user
    public boolean isTokenValid(String token, String emailId) {
        String extracted = extractUsername(token);
        return extracted.equals(emailId) && !isTokenExpired(token);
    }

    // Check expiration
    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expiration.before(new Date());
    }
}
