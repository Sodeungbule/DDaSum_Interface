package com.ddasum.core.auth.service;

import com.ddasum.core.auth.dto.LoginRequest;
import com.ddasum.core.auth.dto.LoginResponse;
import com.ddasum.core.auth.dto.SignupRequest;
import com.ddasum.core.auth.dto.TokenRefreshRequest;
import com.ddasum.core.auth.dto.TokenRefreshResponse;

public interface AuthService {
    
    /**
     * 로그인
     */
    LoginResponse login(LoginRequest loginRequest);
    
    /**
     * 회원가입
     */
    void signup(SignupRequest signupRequest);
    
    /**
     * 로그아웃
     */
    void logout(String accessToken, String refreshToken);
    
    /**
     * 토큰 갱신
     */
    TokenRefreshResponse refreshToken(TokenRefreshRequest refreshRequest);
    
    /**
     * 토큰 유효성 검증
     */
    boolean validateToken(String token);
} 