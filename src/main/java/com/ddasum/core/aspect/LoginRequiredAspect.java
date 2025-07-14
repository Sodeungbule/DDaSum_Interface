package com.ddasum.core.aspect;

import com.ddasum.core.annotation.LoginRequired;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoginRequiredAspect {
    @Around("@within(com.ddasum.core.annotation.LoginRequired) || @annotation(com.ddasum.core.annotation.LoginRequired)")
    public Object checkLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        boolean isLoggedIn = true;
        if (!isLoggedIn) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        return joinPoint.proceed();
    }
} 