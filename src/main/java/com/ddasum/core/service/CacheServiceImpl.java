package com.ddasum.core.service;

import com.ddasum.core.logging.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CacheServiceImpl implements CacheService {
    
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    public CacheServiceImpl() {
        // 매 5분마다 만료된 캐시 정리
        scheduler.scheduleAtFixedRate(this::cleanupExpiredEntries, 5, 5, TimeUnit.MINUTES);
    }
    
    @Override
    public void put(String key, Object value) {
        cache.put(key, new CacheEntry(value, -1));
        LogUtil.logBusiness("캐시 저장: {}", key);
    }
    
    @Override
    public void put(String key, Object value, long ttl, TimeUnit timeUnit) {
        long expirationTime = System.currentTimeMillis() + timeUnit.toMillis(ttl);
        cache.put(key, new CacheEntry(value, expirationTime));
        LogUtil.logBusiness("캐시 저장 (TTL): {} -> {} {}", key, ttl, timeUnit);
    }
    
    @Override
    public <T> Optional<T> get(String key, Class<T> type) {
        CacheEntry entry = cache.get(key);
        if (entry != null && !entry.isExpired()) {
            try {
                @SuppressWarnings("unchecked")
                T value = (T) entry.getValue();
                LogUtil.logBusiness("캐시 조회: {}", key);
                return Optional.of(value);
            } catch (ClassCastException e) {
                log.warn("캐시 타입 불일치: {} -> {}", key, type.getSimpleName());
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
    
    @Override
    public <T> T get(String key, Class<T> type, T defaultValue) {
        return get(key, type).orElse(defaultValue);
    }
    
    @Override
    public void delete(String key) {
        CacheEntry removed = cache.remove(key);
        if (removed != null) {
            LogUtil.logBusiness("캐시 삭제: {}", key);
        }
    }
    
    @Override
    public boolean exists(String key) {
        CacheEntry entry = cache.get(key);
        return entry != null && !entry.isExpired();
    }
    
    @Override
    public void expire(String key, long ttl, TimeUnit timeUnit) {
        CacheEntry entry = cache.get(key);
        if (entry != null) {
            long expirationTime = System.currentTimeMillis() + timeUnit.toMillis(ttl);
            entry.setExpirationTime(expirationTime);
            LogUtil.logBusiness("캐시 만료 시간 설정: {} -> {} {}", key, ttl, timeUnit);
        }
    }
    
    @Override
    public void clear() {
        int size = cache.size();
        cache.clear();
        LogUtil.logBusiness("캐시 전체 삭제: {} 개 항목", size);
    }
    
    @Override
    public long size() {
        return cache.size();
    }
    
    @Override
    public Iterable<String> keys() {
        return cache.keySet();
    }
    
    @Override
    public Iterable<String> keys(String pattern) {
        return cache.keySet().stream()
                .filter(key -> key.matches(pattern))
                .collect(Collectors.toList());
    }
    
    /**
     * 만료된 캐시 항목 정리
     */
    private void cleanupExpiredEntries() {
        long currentTime = System.currentTimeMillis();
        AtomicInteger removedCount = new AtomicInteger(0);
        
        cache.entrySet().removeIf(entry -> {
            if (entry.getValue().isExpired(currentTime)) {
                removedCount.incrementAndGet();
                return true;
            }
            return false;
        });
        
        if (removedCount.get() > 0) {
            log.info("만료된 캐시 항목 {}개 정리 완료", removedCount.get());
        }
    }
    
    /**
     * 캐시 항목 클래스
     */
    private static class CacheEntry {
        private final Object value;
        private long expirationTime;
        
        public CacheEntry(Object value, long expirationTime) {
            this.value = value;
            this.expirationTime = expirationTime;
        }
        
        public Object getValue() {
            return value;
        }
        
        public void setExpirationTime(long expirationTime) {
            this.expirationTime = expirationTime;
        }
        
        public boolean isExpired() {
            return isExpired(System.currentTimeMillis());
        }
        
        public boolean isExpired(long currentTime) {
            return expirationTime > 0 && currentTime > expirationTime;
        }
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