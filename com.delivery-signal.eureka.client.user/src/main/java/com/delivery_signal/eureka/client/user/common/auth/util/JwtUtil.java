package com.delivery_signal.eureka.client.user.common.auth.util;

import com.delivery_signal.eureka.client.user.domain.entity.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // Cookie KEY 값
    public static final String REFRESH_TOKEN_COOKIE = "refreshToken";

    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";

    @Value("${service.jwt.access.secret}")
    private String accessSecret;
    @Value("${service.jwt.access.expiration}")
    private Duration accessExpiration;
    @Value("${service.jwt.refresh.secret}")
    private String refreshSecret;
    @Value("${service.jwt.refresh.expiration}")
    private Duration refreshExpiration;


    private Key accessKey;
    private Key refreshKey;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @Value("${spring.application.name}")
    private String issuer;

    @PostConstruct
    public void init() {
        byte[] accessBytes = Base64.getUrlDecoder().decode(accessSecret);
        byte[] refreshBytes = Base64.getUrlDecoder().decode(refreshSecret);
        accessKey = Keys.hmacShaKeyFor(accessBytes);
        refreshKey = Keys.hmacShaKeyFor(refreshBytes);
    }

    // JWT 토큰 생성
    public String createAccessToken(Long user_id, String username, UserRole role) {
        Date date = new Date();

        return BEARER_PREFIX +
            Jwts.builder()
                .subject(user_id.toString()) // 사용자 식별자값(ID)
                .claim("username", username)
                .claim("user_id", user_id.toString())
                .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                .issuer(issuer) // 발급자
                .expiration(new Date(date.getTime() + accessExpiration.toMillis())) // 만료 시간
                .issuedAt(date) // 발급일
                .signWith(accessKey)
                .compact();
    }

    public String createRefreshToken(Long user_id, String username) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .subject(user_id.toString())
                        .claim("username", username)
                        .claim("user_id", user_id.toString())
                        .issuer(issuer)
                        .expiration(new Date(date.getTime() + accessExpiration.toMillis())) // 만료 시간
                        .issuedAt(date)
                        .signWith(refreshKey)
                        .compact();
    }

    // 생성된 JWT를 Header에 추가
    public void addAccessTokenToHeader(String accessToken, HttpServletResponse response) {
        response.setHeader(AUTHORIZATION_HEADER, accessToken);
    }
    // 생성된 JWT를 Cookie에 추가
    public void addRefreshTokenToCookie(String refreshToken, HttpServletResponse response) {
        try {
            refreshToken = URLEncoder.encode(refreshToken, "utf-8").replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행

            Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE, refreshToken); // Name-Value
            cookie.setPath("/");

            // Response 객체에 Cookie 추가
            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }

    // JWT 토큰 substring
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(BEARER_PREFIX.length());
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    // 토큰 검증
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().verifyWith((SecretKey) accessKey).build().parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException  e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parser().verifyWith((SecretKey) refreshKey).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException  e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보(Claim) 가져오기
    public Claims getUserInfoFromAccessToken(String token) {
        return Jwts.parser().verifyWith((SecretKey) accessKey).build().parseSignedClaims(token).getPayload();
    }

    public Claims getUserInfoFromRefreshToken(String token) {
        return Jwts.parser().verifyWith((SecretKey) refreshKey).build().parseSignedClaims(token).getPayload();
    }

    // HttpServletRequest에서 Access 토큰 가져오기
    public String getAccessTokenFromHeader(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(token)) {
            return token;
        }
        return null;
    }

    // Cookie에서 Refresh Token 가져오기
    public String getRefreshTokenFromHeader(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(REFRESH_TOKEN_COOKIE)) {
                    try {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return null;
    }

    // 응답에서 Cookie 삭제
    public void deleteCookieFromResponse(HttpServletResponse response) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

    }
}
