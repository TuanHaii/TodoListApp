package com.example.todolist.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {
    @Value("${jwt.secret}")
    private String  secret; // Use a strong secret key in production

    // Method sinh JWT token moi cho user
        public String generateToken(String username) {
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    // Method xay dung JWT token voi cac thong tin: subject (username), issuedAt, expiration, claims (neu co)
    private String createToken(Map<String,Object> claims, String email) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    // Method chuyen secret string thanh key de ky token va validate token
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    // Method trich xuat thong tin username (subject) va expiration tu token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    // Lay thoi diem het han tu token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Method Generic de trich xuat cac thong tin tu token su dung claimsResolver function
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    // Phuong thuc trich xuat tat ca claims tu token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    // Kiem tra token con han khong
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    // Kiem tra token hop le voi userDetails (username trung khop va chua het han)
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}

