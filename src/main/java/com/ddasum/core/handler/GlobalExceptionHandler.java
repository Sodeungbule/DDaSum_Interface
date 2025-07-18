package com.ddasum.core.handler;

import com.ddasum.core.api.response.ApiResponse;
import com.ddasum.core.error.dto.ErrorResponse;
import com.ddasum.core.error.enums.ErrorCode;
import com.ddasum.core.exception.AuthenticationException;
import com.ddasum.core.exception.BusinessException;
import com.ddasum.core.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * 비즈니스 로직 예외 처리
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleBusinessException(
            BusinessException e, HttpServletRequest request) {
        log.error("BusinessException: {}", e.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(
            e.getErrorCode(), 
            request.getRequestURI()
        );
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(errorResponse.getMessage(), String.valueOf(errorResponse.getCode())));
    }
    
    /**
     * 인증 예외 처리
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleAuthenticationException(
            AuthenticationException e, HttpServletRequest request) {
        log.error("AuthenticationException: {}", e.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(
            e.getErrorCode(), 
            request.getRequestURI()
        );
        
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse.error(errorResponse.getMessage(), String.valueOf(errorResponse.getCode())));
    }
    
    /**
     * 리소스 찾을 수 없음 예외 처리
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleResourceNotFoundException(
            ResourceNotFoundException e, HttpServletRequest request) {
        log.error("ResourceNotFoundException: {}", e.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(
            ErrorCode.ENTITY_NOT_FOUND, 
            request.getRequestURI(),
            e.getMessage()
        );
        
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(errorResponse.getMessage(), String.valueOf(errorResponse.getCode())));
    }
    
    /**
     * Spring Security 인증 예외 처리
     */
    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleSpringAuthenticationException(
            org.springframework.security.core.AuthenticationException e, HttpServletRequest request) {
        log.error("Spring AuthenticationException: {}", e.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(
            ErrorCode.INVALID_CREDENTIALS, 
            request.getRequestURI()
        );
        
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse.error(errorResponse.getMessage(), String.valueOf(errorResponse.getCode())));
    }
    
    /**
     * Spring Security 권한 예외 처리
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleAccessDeniedException(
            AccessDeniedException e, HttpServletRequest request) {
        log.error("AccessDeniedException: {}", e.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(
            ErrorCode.FORBIDDEN, 
            request.getRequestURI()
        );
        
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(ApiResponse.error(errorResponse.getMessage(), String.valueOf(errorResponse.getCode())));
    }
    
    /**
     * 잘못된 인증 정보 예외 처리
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleBadCredentialsException(
            BadCredentialsException e, HttpServletRequest request) {
        log.error("BadCredentialsException: {}", e.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(
            ErrorCode.INVALID_CREDENTIALS, 
            request.getRequestURI()
        );
        
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse.error(errorResponse.getMessage(), String.valueOf(errorResponse.getCode())));
    }
    
    /**
     * 유효성 검증 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleValidationException(
            MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("ValidationException: {}", e.getMessage());
        
        List<String> errors = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.toList());
        
        String errorMessage = String.join(", ", errors);
        
        ErrorResponse errorResponse = ErrorResponse.of(
            ErrorCode.INVALID_INPUT_VALUE, 
            request.getRequestURI(),
            errorMessage
        );
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(errorResponse.getMessage(), String.valueOf(errorResponse.getCode())));
    }
    
    /**
     * 바인딩 예외 처리
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleBindException(
            BindException e, HttpServletRequest request) {
        log.error("BindException: {}", e.getMessage());
        
        List<String> errors = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.toList());
        
        String errorMessage = String.join(", ", errors);
        
        ErrorResponse errorResponse = ErrorResponse.of(
            ErrorCode.INVALID_INPUT_VALUE, 
            request.getRequestURI(),
            errorMessage
        );
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(errorResponse.getMessage(), String.valueOf(errorResponse.getCode())));
    }
    
    /**
     * 메서드 인자 타입 불일치 예외 처리
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.error("MethodArgumentTypeMismatchException: {}", e.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(
            ErrorCode.INVALID_INPUT_VALUE, 
            request.getRequestURI(),
            "Parameter '" + e.getName() + "' should be of type " + e.getRequiredType().getSimpleName()
        );
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(errorResponse.getMessage(), String.valueOf(errorResponse.getCode())));
    }
    
    /**
     * 지원하지 않는 HTTP 메서드 예외 처리
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.error("HttpRequestMethodNotSupportedException: {}", e.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(
            ErrorCode.METHOD_NOT_ALLOWED, 
            request.getRequestURI()
        );
        
        return ResponseEntity
            .status(HttpStatus.METHOD_NOT_ALLOWED)
            .body(ApiResponse.error(errorResponse.getMessage(), String.valueOf(errorResponse.getCode())));
    }
    
    /**
     * 핸들러 찾을 수 없음 예외 처리
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleNoHandlerFoundException(
            NoHandlerFoundException e, HttpServletRequest request) {
        log.error("NoHandlerFoundException: {}", e.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(
            ErrorCode.ENTITY_NOT_FOUND, 
            request.getRequestURI()
        );
        
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(errorResponse.getMessage(), String.valueOf(errorResponse.getCode())));
    }
    
    /**
     * 기타 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleException(
            Exception e, HttpServletRequest request) {
        log.error("Unexpected error occurred: {}", e.getMessage(), e);
        
        ErrorResponse errorResponse = ErrorResponse.of(
            ErrorCode.INTERNAL_SERVER_ERROR, 
            request.getRequestURI()
        );
        
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(errorResponse.getMessage(), String.valueOf(errorResponse.getCode())));
    }
} 