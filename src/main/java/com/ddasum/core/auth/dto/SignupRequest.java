package com.ddasum.core.auth.dto;

import com.ddasum.core.annotation.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    
    @NotBlank(message = "이름은 필수입니다")
    @Size(min = 2, max = 50, message = "이름은 2자 이상 50자 이하여야 합니다")
    private String name;
    
    @NotBlank(message = "아이디는 필수입니다")
    @Size(min = 2, max = 50, message = "아이디는 2자 이상 50자 이하여야 합니다")
    private String username;
    
    @NotBlank(message = "이메일은 필수입니다")
    @ValidEmail
    private String email;
    
    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 8, max = 100, message = "비밀번호는 8자 이상 100자 이하여야 합니다")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", 
             message = "비밀번호는 영문 대소문자, 숫자, 특수문자를 포함해야 합니다")
    private String password;
    
    @NotBlank(message = "비밀번호 확인은 필수입니다")
    private String confirmPassword;

    @NotBlank(message = "이메일 인증코드는 필수입니다")
    private String verificationCode;
} 