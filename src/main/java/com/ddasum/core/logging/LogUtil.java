package com.ddasum.core.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;

@Slf4j
public class LogUtil {
    
    private static final String REQUEST_ID_HEADER = "X-Request-ID";
    private static final String REQUEST_ID_ATTRIBUTE = "requestId";
    
    /**
     * 요청 ID 생성
     */
    public static String generateRequestId() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * 현재 요청 ID 가져오기
     */
    public static String getCurrentRequestId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String requestId = (String) request.getAttribute(REQUEST_ID_ATTRIBUTE);
            if (requestId == null) {
                requestId = request.getHeader(REQUEST_ID_HEADER);
                if (requestId == null) {
                    requestId = generateRequestId();
                }
                request.setAttribute(REQUEST_ID_ATTRIBUTE, requestId);
            }
            return requestId;
        }
        return generateRequestId();
    }
    
    /**
     * API 요청 로그
     */
    public static void logApiRequest(String method, String path, Object requestBody) {
        String requestId = getCurrentRequestId();
        log.info("[{}] API Request - Method: {}, Path: {}, Body: {}", 
                requestId, method, path, requestBody);
    }
    
    /**
     * API 응답 로그
     */
    public static void logApiResponse(String method, String path, Object responseBody, long duration) {
        String requestId = getCurrentRequestId();
        log.info("[{}] API Response - Method: {}, Path: {}, Duration: {}ms, Body: {}", 
                requestId, method, path, duration, responseBody);
    }
    
    /**
     * 에러 로그
     */
    public static void logError(String message, Throwable throwable) {
        String requestId = getCurrentRequestId();
        log.error("[{}] Error: {}", requestId, message, throwable);
    }
    
    /**
     * 비즈니스 로직 로그
     */
    public static void logBusiness(String message, Object... args) {
        String requestId = getCurrentRequestId();
        log.info("[{}] Business: {}", requestId, String.format(message, args));
    }
    
    /**
     * 디버그 로그
     */
    public static void logDebug(String message, Object... args) {
        String requestId = getCurrentRequestId();
        log.debug("[{}] Debug: {}", requestId, String.format(message, args));
    }
    
    /**
     * 성능 측정 로그
     */
    public static void logPerformance(String operation, long duration) {
        String requestId = getCurrentRequestId();
        log.info("[{}] Performance - Operation: {}, Duration: {}ms", 
                requestId, operation, duration);
    }
    
    /**
     * 데이터베이스 쿼리 로그
     */
    public static void logDatabaseQuery(String query, Object[] params, long duration) {
        String requestId = getCurrentRequestId();
        log.debug("[{}] Database Query - SQL: {}, Params: {}, Duration: {}ms", 
                requestId, query, params, duration);
    }
    
    /**
     * 외부 API 호출 로그
     */
    public static void logExternalApiCall(String url, String method, Object requestBody, long duration) {
        String requestId = getCurrentRequestId();
        log.info("[{}] External API Call - URL: {}, Method: {}, Duration: {}ms, Request: {}", 
                requestId, url, method, duration, requestBody);
    }
    
    /**
     * 파일 업로드 로그
     */
    public static void logFileUpload(String fileName, long fileSize, String uploadPath) {
        String requestId = getCurrentRequestId();
        log.info("[{}] File Upload - Name: {}, Size: {} bytes, Path: {}", 
                requestId, fileName, fileSize, uploadPath);
    }
    
    /**
     * 사용자 액션 로그
     */
    public static void logUserAction(String userId, String action, String details) {
        String requestId = getCurrentRequestId();
        log.info("[{}] User Action - User: {}, Action: {}, Details: {}", 
                requestId, userId, action, details);
    }
    
    /**
     * 보안 관련 로그
     */
    public static void logSecurity(String event, String details) {
        String requestId = getCurrentRequestId();
        log.warn("[{}] Security Event - Event: {}, Details: {}", 
                requestId, event, details);
    }
    
    /**
     * 메모리 사용량 로그
     */
    public static void logMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        String requestId = getCurrentRequestId();
        log.debug("[{}] Memory Usage - Total: {} MB, Used: {} MB, Free: {} MB", 
                requestId, 
                totalMemory / (1024 * 1024), 
                usedMemory / (1024 * 1024), 
                freeMemory / (1024 * 1024));
    }
} 