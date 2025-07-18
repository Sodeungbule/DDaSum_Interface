package com.ddasum.core.controller;

import com.ddasum.core.api.response.ApiResponse;
import com.ddasum.core.auth.dto.LoginRequest;
import com.ddasum.core.auth.dto.LoginResponse;
import com.ddasum.core.auth.dto.SignupRequest;
import com.ddasum.core.auth.dto.TokenRefreshRequest;
import com.ddasum.core.auth.dto.TokenRefreshResponse;
import com.ddasum.core.auth.service.AuthService;
import com.ddasum.core.constants.CommonConstants;
import com.ddasum.core.service.EmailVerificationService;
import com.ddasum.core.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(CommonConstants.API_BASE_PATH + "/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    private final EmailVerificationService emailVerificationService;
    private final EmailService emailService;
    
    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
    
    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody SignupRequest signupRequest) {
        authService.signup(signupRequest);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
    
    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        String accessToken = getTokenFromRequest(request, CommonConstants.JWT_HEADER_NAME);
        String refreshToken = getTokenFromRequest(request, CommonConstants.REFRESH_TOKEN_HEADER_NAME);
        
        authService.logout(accessToken, refreshToken);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
    
    /**
     * 토큰 갱신
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenRefreshResponse>> refreshToken(@Valid @RequestBody TokenRefreshRequest refreshRequest) {
        TokenRefreshResponse response = authService.refreshToken(refreshRequest);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
    
    /**
     * 토큰 유효성 검증
     */
    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<Boolean>> validateToken(HttpServletRequest request) {
        String token = getTokenFromRequest(request, CommonConstants.JWT_HEADER_NAME);
        boolean isValid = authService.validateToken(token);
        return ResponseEntity.ok(ApiResponse.ok(isValid));
    }

    /**
     * 이메일 인증코드 발송
     */
    @PostMapping("/send-verification-code")
    public ResponseEntity<ApiResponse<Void>> sendVerificationCode(@RequestParam String email) {
        String code = emailVerificationService.generateVerificationCode();
        emailVerificationService.saveVerificationCode(email, code);
        emailService.sendVerificationEmail(email, code);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    /**
     * 이메일 인증코드 검증
     */
    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<Boolean>> verifyEmail(@RequestParam String email, @RequestParam String code) {
        boolean result = emailVerificationService.verifyCode(email, code);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }
    
    /**
     * 요청에서 토큰 추출
     */
    private String getTokenFromRequest(HttpServletRequest request, String headerName) {
        String bearerToken = request.getHeader(headerName);
        
        if (bearerToken != null && bearerToken.startsWith(CommonConstants.JWT_TOKEN_PREFIX)) {
            return bearerToken.substring(CommonConstants.JWT_TOKEN_PREFIX.length());
        }
        
        return null;
    }
} 