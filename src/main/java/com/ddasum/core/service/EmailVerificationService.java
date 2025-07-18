package com.ddasum.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final long VERIFICATION_CODE_TTL_MINUTES = 5;

    public String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6자리 숫자
        return String.valueOf(code);
    }

    public void saveVerificationCode(String email, String code) {
        redisTemplate.opsForValue().set(getKey(email), code, VERIFICATION_CODE_TTL_MINUTES, TimeUnit.MINUTES);
    }

    public boolean verifyCode(String email, String code) {
        String key = getKey(email);
        String storedCode = redisTemplate.opsForValue().get(key);
        if (storedCode != null && storedCode.equals(code)) {
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }

    public void deleteCode(String email) {
        redisTemplate.delete(getKey(email));
    }

    private String getKey(String email) {
        return "email:verification:" + email;
    }
} 