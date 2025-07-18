package com.ddasum.core.auth.service;

import com.ddasum.core.auth.dto.LoginRequest;
import com.ddasum.core.auth.dto.LoginResponse;
import com.ddasum.core.auth.dto.SignupRequest;
import com.ddasum.core.auth.dto.TokenRefreshRequest;
import com.ddasum.core.auth.dto.TokenRefreshResponse;
import com.ddasum.core.auth.jwt.JwtTokenProvider;
import com.ddasum.core.constants.CommonConstants;
import com.ddasum.core.exception.AuthenticationException;
import com.ddasum.core.error.enums.ErrorCode;
import com.ddasum.core.service.EmailVerificationService;
import com.ddasum.domain.user.entity.User;
import com.ddasum.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final TokenBlacklistService tokenBlacklistService;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final EmailVerificationService emailVerificationService;
    
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), 
                    loginRequest.getPassword()
                )
            );
            
            String accessToken = jwtTokenProvider.generateAccessToken(authentication);
            String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType(CommonConstants.JWT_TOKEN_PREFIX.trim())
                    .accessTokenExpiresIn(CommonConstants.JWT_ACCESS_TOKEN_EXPIRATION_TIME)
                    .refreshTokenExpiresIn(CommonConstants.JWT_REFRESH_TOKEN_EXPIRATION_TIME)
                    .username(userDetails.getUsername())
                    .email(userDetails.getUsername())
                    .role(userDetails.getAuthorities().iterator().next().getAuthority())
                    .build();
                    
        } catch (Exception e) {
            log.error("로그인 실패: {}", e.getMessage());
            throw new AuthenticationException(ErrorCode.INVALID_CREDENTIALS);
        }
    }
    
    @Override
    public void signup(SignupRequest signupRequest) {
        // 비밀번호 확인
        if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
            throw new AuthenticationException(ErrorCode.INVALID_INPUT_VALUE, "비밀번호가 일치하지 않습니다");
        }
        // 이메일 인증 여부 확인
        if (!emailVerificationService.verifyCode(signupRequest.getEmail(), signupRequest.getVerificationCode())) {
            throw new AuthenticationException(ErrorCode.INVALID_INPUT_VALUE, "이메일 인증이 완료되지 않았습니다");
        }
        // 이메일/아이디 중복 체크
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new AuthenticationException(ErrorCode.DUPLICATE_RESOURCE, "이미 사용 중인 이메일입니다");
        }
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new AuthenticationException(ErrorCode.DUPLICATE_RESOURCE, "이미 사용 중인 아이디입니다");
        }
        // User 저장
        User user = User.builder()
            .username(signupRequest.getUsername())
            .email(signupRequest.getEmail())
            .password(passwordEncoder.encode(signupRequest.getPassword()))
            .role("USER") // 'ROLE_USER'가 아니라 'USER'로 저장
            .emailVerified(true)
            .build();
        userRepository.save(user);
        log.info("회원가입 완료: {}", signupRequest.getEmail());
    }
    
    @Override
    public void logout(String accessToken, String refreshToken) {
        // 토큰을 블랙리스트에 추가
        if (accessToken != null) {
            long accessTokenExpiration = jwtTokenProvider.getExpirationDateFromToken(accessToken).getTime();
            tokenBlacklistService.addToBlacklist(accessToken, accessTokenExpiration);
        }
        if (refreshToken != null) {
            long refreshTokenExpiration = jwtTokenProvider.getExpirationDateFromToken(refreshToken).getTime();
            tokenBlacklistService.addToBlacklist(refreshToken, refreshTokenExpiration);
        }
        
        log.info("로그아웃 완료");
    }
    
    @Override
    public TokenRefreshResponse refreshToken(TokenRefreshRequest refreshRequest) {
        try {
            // Refresh Token 검증
            if (!jwtTokenProvider.validateRefreshToken(refreshRequest.getRefreshToken())) {
                throw new AuthenticationException(ErrorCode.INVALID_TOKEN, "유효하지 않은 Refresh Token입니다");
            }
            
            // 블랙리스트 확인
            if (tokenBlacklistService.isBlacklisted(refreshRequest.getRefreshToken())) {
                throw new AuthenticationException(ErrorCode.INVALID_TOKEN, "블랙리스트에 등록된 토큰입니다");
            }
            
            // 사용자 정보 추출
            String username = jwtTokenProvider.getUsernameFromToken(refreshRequest.getRefreshToken());
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            // 새로운 인증 객체 생성
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
            );
            
            // 새로운 토큰 생성
            String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(authentication);
            
            // 기존 Refresh Token을 블랙리스트에 추가 (토큰 재사용 방지)
            long oldRefreshTokenExpiration = jwtTokenProvider.getExpirationDateFromToken(refreshRequest.getRefreshToken()).getTime();
            tokenBlacklistService.addToBlacklist(refreshRequest.getRefreshToken(), oldRefreshTokenExpiration);
            
            return TokenRefreshResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .tokenType(CommonConstants.JWT_TOKEN_PREFIX.trim())
                    .accessTokenExpiresIn(CommonConstants.JWT_ACCESS_TOKEN_EXPIRATION_TIME)
                    .refreshTokenExpiresIn(CommonConstants.JWT_REFRESH_TOKEN_EXPIRATION_TIME)
                    .build();
            
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            log.error("토큰 갱신 실패: {}", e.getMessage());
            throw new AuthenticationException(ErrorCode.INVALID_TOKEN);
        }
    }
    
    @Override
    public boolean validateToken(String token) {
        try {
            // 블랙리스트 확인
            if (tokenBlacklistService.isBlacklisted(token)) {
                return false;
            }
            
            return jwtTokenProvider.validateAccessToken(token);
        } catch (Exception e) {
            return false;
        }
    }
} 