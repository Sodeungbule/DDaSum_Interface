package com.ddasum.core.controller;

import com.ddasum.core.api.response.ApiResponse;
import com.ddasum.core.constants.CommonConstants;
import com.ddasum.core.logging.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping(CommonConstants.API_BASE_PATH)
public abstract class BaseController {

    /**
     * 성공 응답 생성
     */
    protected <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    /**
     * 성공 응답 생성 (메시지 포함)
     */
    protected <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        ApiResponse<T> response = ApiResponse.ok(data);
        response.setMessage(message);
        return ResponseEntity.ok(response);
    }

    /**
     * 페이징 응답 생성
     */
    protected <T> ResponseEntity<ApiResponse<Page<T>>> successPage(Page<T> page) {
        return ResponseEntity.ok(ApiResponse.ok(page));
    }

    /**
     * 현재 인증된 사용자명 가져오기
     */
    protected String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

    /**
     * 현재 인증된 사용자 정보 가져오기
     */
    protected Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 요청 로깅
     */
    protected void logRequest(String method, String path, Object requestBody) {
        LogUtil.logApiRequest(method, path, requestBody);
    }

    /**
     * 응답 로깅
     */
    protected void logResponse(String method, String path, Object responseBody, long duration) {
        LogUtil.logApiResponse(method, path, responseBody, duration);
    }

    /**
     * 비즈니스 로직 로깅
     */
    protected void logBusiness(String message, Object... args) {
        LogUtil.logBusiness(message, args);
    }

    /**
     * JSON 테스트용 서비스
     */
    @RestController
    @RequestMapping("/json")
    public static class JsonService {
        
        @PostMapping
        public Map<String, Object> jsonMap(@RequestBody Map<String, Object> data) {
            return data; // 데이터 받을 때 HashMap으로 받아지도록
        }
        
        @GetMapping("/test")
        public Map<String, Object> testJson() {
            return Map.of(
                "message", "JSON 테스트 성공",
                "timestamp", System.currentTimeMillis(),
                "status", "OK"
            );
        }
    }

    /**
     * 헬스체크용 서비스
     */
    @RestController
    @RequestMapping("/health")
    public static class HealthService {
        
        @GetMapping
        public Map<String, Object> health() {
            return Map.of(
                "status", "UP",
                "timestamp", System.currentTimeMillis(),
                "service", "DDaSum API"
            );
        }
        
        @GetMapping("/ping")
        public Map<String, String> ping() {
            return Map.of("message", "pong");
        }
    }
}
