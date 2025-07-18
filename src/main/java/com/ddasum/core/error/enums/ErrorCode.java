package com.ddasum.core.error.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    
    // 공통 에러 (1000번대)
    INVALID_INPUT_VALUE(1000, "잘못된 입력값입니다"),
    METHOD_NOT_ALLOWED(1001, "허용되지 않은 메서드입니다"),
    ENTITY_NOT_FOUND(1002, "요청한 리소스를 찾을 수 없습니다"),
    INTERNAL_SERVER_ERROR(1003, "서버 내부 오류가 발생했습니다"),
    DUPLICATE_RESOURCE(1004, "이미 존재하는 리소스입니다"),
    
    // 인증/인가 에러 (2000번대)
    UNAUTHORIZED(2000, "인증이 필요합니다"),
    FORBIDDEN(2001, "접근 권한이 없습니다"),
    INVALID_TOKEN(2002, "유효하지 않은 토큰입니다"),
    EXPIRED_TOKEN(2003, "만료된 토큰입니다"),
    INVALID_CREDENTIALS(2004, "잘못된 인증 정보입니다"),
    
    // 사용자 관련 에러 (3000번대)
    USER_NOT_FOUND(3000, "사용자를 찾을 수 없습니다"),
    USER_ALREADY_EXISTS(3001, "이미 존재하는 사용자입니다"),
    INVALID_EMAIL_FORMAT(3002, "올바르지 않은 이메일 형식입니다"),
    INVALID_PASSWORD_FORMAT(3003, "올바르지 않은 비밀번호 형식입니다"),
    
    // 데이터 관련 에러 (4000번대)
    DATA_NOT_FOUND(4000, "데이터를 찾을 수 없습니다"),
    DATA_ALREADY_EXISTS(4001, "이미 존재하는 데이터입니다"),
    DATA_INTEGRITY_VIOLATION(4002, "데이터 무결성 위반입니다"),
    
    // 파일 관련 에러 (5000번대)
    FILE_NOT_FOUND(5000, "파일을 찾을 수 없습니다"),
    FILE_TOO_LARGE(5001, "파일 크기가 너무 큽니다"),
    INVALID_FILE_TYPE(5002, "지원하지 않는 파일 형식입니다"),
    FILE_UPLOAD_FAILED(5003, "파일 업로드에 실패했습니다"),
    
    // 비즈니스 로직 에러 (6000번대)
    BUSINESS_RULE_VIOLATION(6000, "비즈니스 규칙 위반입니다"),
    INSUFFICIENT_PERMISSIONS(6001, "권한이 부족합니다"),
    OPERATION_NOT_ALLOWED(6002, "허용되지 않은 작업입니다");
    
    private final int code;
    private final String message;
} 