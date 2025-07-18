package com.ddasum.core.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long accessTokenExpiresIn;
    private Long refreshTokenExpiresIn;
    private String username;
    private String email;
    private String role;
} 