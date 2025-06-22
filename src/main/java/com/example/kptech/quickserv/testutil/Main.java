package com.example.kptech.quickserv.testutil;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        // Replace with the same key you have in application.properties
        String secret = "secretkeysecretkeysecretkey12"; // Must be 32+ chars
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        // Generate a JWT token
        String token = Jwts.builder()
                .setSubject("test@example.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        System.out.println("Generated Token:");
        System.out.println(token);

        // Decode the token
        String subject = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        System.out.println("Extracted Subject:");
        System.out.println(subject);
    }
}
