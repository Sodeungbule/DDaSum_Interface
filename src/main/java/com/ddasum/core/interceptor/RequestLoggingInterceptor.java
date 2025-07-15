package com.ddasum.core.interceptor;

import com.ddasum.core.logging.LogUtil;
import com.ddasum.core.util.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Slf4j
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {
    
    private static final String START_TIME_ATTRIBUTE = "startTime";
    private static final String REQUEST_ID_ATTRIBUTE = "requestId";
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 요청 시작 시간 기록
        request.setAttribute(START_TIME_ATTRIBUTE, System.currentTimeMillis());
        
        // 요청 ID 설정
        String requestId = getOrCreateRequestId(request);
        request.setAttribute(REQUEST_ID_ATTRIBUTE, requestId);
        
        // 요청 로그 기록
        String method = request.getMethod();
        String path = request.getRequestURI();
        String queryString = request.getQueryString();
        String fullPath = queryString != null ? path + "?" + queryString : path;
        
        LogUtil.logApiRequest(method, fullPath, null);
        
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 응답 상태 코드 로깅
        int statusCode = response.getStatus();
        String requestId = (String) request.getAttribute(REQUEST_ID_ATTRIBUTE);
        
        if (statusCode >= 400) {
            log.warn("[{}] Response Status: {}", requestId, statusCode);
        }
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 요청 처리 시간 계산
        Long startTime = (Long) request.getAttribute(START_TIME_ATTRIBUTE);
        long duration = startTime != null ? System.currentTimeMillis() - startTime : 0;
        
        // 응답 로그 기록
        String method = request.getMethod();
        String path = request.getRequestURI();
        int statusCode = response.getStatus();
        String requestId = (String) request.getAttribute(REQUEST_ID_ATTRIBUTE);
        
        LogUtil.logApiResponse(method, path, "Status: " + statusCode, duration);
        
        // 성능 로그 기록
        if (duration > 1000) { // 1초 이상 걸린 요청
            LogUtil.logPerformance("API Request", duration);
        }
        
        // 에러 로그 기록
        if (ex != null) {
            LogUtil.logError("Request processing failed", ex);
        }
    }
    
    private String getOrCreateRequestId(HttpServletRequest request) {
        String requestId = request.getHeader("X-Request-ID");
        if (StringUtil.isEmpty(requestId)) {
            requestId = UUID.randomUUID().toString();
        }
        return requestId;
    }
} 