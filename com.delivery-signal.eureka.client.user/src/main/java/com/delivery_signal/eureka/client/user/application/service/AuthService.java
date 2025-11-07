package com.delivery_signal.eureka.client.user.application.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class AuthService {

    @Value("${spring.application.name}")
    private String issuer;

    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;

    private final SecretKey secretKey;

    public AuthService(@Value("${service.jwt.secret-key}") String secretKey) { // secretKey Base64 URL 인코딩된 비밀 키
        // HMAC-SHA 알고리즘에 적합한 SecretKy 객체 생성
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey)); // Base64 URL 인코딩된 비밀 키를 디코딩
    }

    // Access 토큰 생성
    public String createAccessToken(String userId) {
        return Jwts.builder()
                .claim("user_id", userId)
//                .claim("role", role)
                .issuer(issuer)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

    }
}
