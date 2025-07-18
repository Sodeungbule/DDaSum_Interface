package com.ddasum.core.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TokenBlacklistService {
    
    private final ConcurrentHashMap<String, Long> blacklistedTokens = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    public TokenBlacklistService() {
        // 매일 자정에 만료된 토큰들을 정리
        scheduler.scheduleAtFixedRate(this::cleanupExpiredTokens, 24, 24, TimeUnit.HOURS);
    }
    
    /**
     * 토큰을 블랙리스트에 추가
     */
    public void addToBlacklist(String token, long expirationTime) {
        blacklistedTokens.put(token, expirationTime);
        log.info("토큰이 블랙리스트에 추가되었습니다: {}", token.substring(0, Math.min(10, token.length())) + "...");
    }
    
    /**
     * 토큰이 블랙리스트에 있는지 확인
     */
    public boolean isBlacklisted(String token) {
        Long expirationTime = blacklistedTokens.get(token);
        if (expirationTime == null) {
            return false;
        }
        
        // 만료된 토큰이면 블랙리스트에서 제거
        if (System.currentTimeMillis() > expirationTime) {
            blacklistedTokens.remove(token);
            return false;
        }
        
        return true;
    }
    
    /**
     * 만료된 토큰들을 정리
     */
    private void cleanupExpiredTokens() {
        long currentTime = System.currentTimeMillis();
        int removedCount = 0;
        
        for (var entry : blacklistedTokens.entrySet()) {
            if (currentTime > entry.getValue()) {
                blacklistedTokens.remove(entry.getKey());
                removedCount++;
            }
        }
        
        if (removedCount > 0) {
            log.info("블랙리스트에서 {}개의 만료된 토큰이 제거되었습니다", removedCount);
        }
    }
    
    /**
     * 블랙리스트 크기 반환
     */
    public int getBlacklistSize() {
        return blacklistedTokens.size();
    }
    
    /**
     * 서비스 종료 시 정리
     */
    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
} 