package com.ddasum.core.aspect;

import com.ddasum.core.annotation.AdminOnly;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AdminOnlyAspect {
    @Around("@within(com.ddasum.core.annotation.AdminOnly) || @annotation(com.ddasum.core.annotation.AdminOnly)")
    public Object checkAdmin(ProceedingJoinPoint joinPoint) throws Throwable {
        boolean isAdmin = true;
        if (!isAdmin) {
            throw new RuntimeException("관리자 권한이 필요합니다.");
        }
        return joinPoint.proceed();
    }
} 