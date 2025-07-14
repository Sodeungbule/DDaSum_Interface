package com.ddasum.core.aspect;

import com.ddasum.core.annotation.TraceLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TraceLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(TraceLogAspect.class);

    @Around("@within(com.ddasum.core.annotation.TraceLog) || @annotation(com.ddasum.core.annotation.TraceLog)")
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        logger.info("[TraceLog] 시작: {}", methodName);
        Object result = joinPoint.proceed();
        logger.info("[TraceLog] 종료: {}", methodName);
        return result;
    }
} 