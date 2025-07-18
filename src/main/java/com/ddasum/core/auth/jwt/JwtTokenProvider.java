package com.ddasum.core.auth.jwt;

import com.ddasum.core.constants.CommonConstants;
import com.ddasum.core.exception.AuthenticationException;
import com.ddasum.core.error.enums.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtTokenProvider {
    
    private final SecretKey secretKey;
    
    public JwtTokenProvider(@Value("${ddasum.jwt.secret}") String jwtSecret) {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
    
    /**
     * Access Token 생성
     */
    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, TokenType.ACCESS_TOKEN, CommonConstants.JWT_ACCESS_TOKEN_EXPIRATION_TIME);
    }
    
    /**
     * Refresh Token 생성
     */
    public String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication, TokenType.REFRESH_TOKEN, CommonConstants.JWT_REFRESH_TOKEN_EXPIRATION_TIME);
    }
    
    /**
     * JWT 토큰 생성 (공통)
     */
    private String generateToken(Authentication authentication, TokenType tokenType, long expirationTime) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", userDetails.getAuthorities());
        claims.put("tokenType", tokenType.name());
        
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .addClaims(claims)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }
    
    /**
     * JWT 토큰에서 사용자명 추출
     */
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            
            return claims.getSubject();
        } catch (JwtException e) {
            log.error("JWT 토큰에서 사용자명 추출 실패: {}", e.getMessage());
            throw new AuthenticationException(ErrorCode.INVALID_TOKEN);
        }
    }
    
    /**
     * JWT 토큰에서 토큰 타입 추출
     */
    public TokenType getTokenType(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            
            String tokenTypeStr = claims.get("tokenType", String.class);
            return TokenType.valueOf(tokenTypeStr);
        } catch (JwtException e) {
            log.error("JWT 토큰에서 토큰 타입 추출 실패: {}", e.getMessage());
            throw new AuthenticationException(ErrorCode.INVALID_TOKEN);
        }
    }
    
    /**
     * JWT 토큰 유효성 검증
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("JWT 토큰 만료: {}", e.getMessage());
            throw new AuthenticationException(ErrorCode.EXPIRED_TOKEN);
        } catch (JwtException e) {
            log.error("JWT 토큰 검증 실패: {}", e.getMessage());
            throw new AuthenticationException(ErrorCode.INVALID_TOKEN);
        }
    }
    
    /**
     * Access Token 유효성 검증
     */
    public boolean validateAccessToken(String token) {
        if (!validateToken(token)) {
            return false;
        }
        
        TokenType tokenType = getTokenType(token);
        if (tokenType != TokenType.ACCESS_TOKEN) {
            throw new AuthenticationException(ErrorCode.INVALID_TOKEN, "Access Token이 아닙니다");
        }
        
        return true;
    }
    
    /**
     * Refresh Token 유효성 검증
     */
    public boolean validateRefreshToken(String token) {
        if (!validateToken(token)) {
            return false;
        }
        
        TokenType tokenType = getTokenType(token);
        if (tokenType != TokenType.REFRESH_TOKEN) {
            throw new AuthenticationException(ErrorCode.INVALID_TOKEN, "Refresh Token이 아닙니다");
        }
        
        return true;
    }
    
    /**
     * JWT 토큰에서 만료일 추출
     */
    public Date getExpirationDateFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            
            return claims.getExpiration();
        } catch (JwtException e) {
            log.error("JWT 토큰에서 만료일 추출 실패: {}", e.getMessage());
            throw new AuthenticationException(ErrorCode.INVALID_TOKEN);
        }
    }
    
    /**
     * 토큰이 만료되었는지 확인
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
    
    /**
     * 토큰에서 모든 클레임 추출
     */
    public Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            log.error("JWT 토큰에서 클레임 추출 실패: {}", e.getMessage());
            throw new AuthenticationException(ErrorCode.INVALID_TOKEN);
        }
    }
} 