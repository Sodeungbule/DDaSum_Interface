package com.ddasum.core.exception;

import com.ddasum.core.error.enums.ErrorCode;

public class AuthenticationException extends BusinessException {
    
    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }
    
    public AuthenticationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
    
    public AuthenticationException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
    
    public AuthenticationException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
} 