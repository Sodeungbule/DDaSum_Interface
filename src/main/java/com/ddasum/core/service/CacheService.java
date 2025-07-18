package com.ddasum.core.service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public interface CacheService {
    
    /**
     * 캐시에 데이터 저장
     */
    void put(String key, Object value);
    
    /**
     * 캐시에 데이터 저장 (TTL 포함)
     */
    void put(String key, Object value, long ttl, TimeUnit timeUnit);
    
    /**
     * 캐시에서 데이터 조회
     */
    <T> Optional<T> get(String key, Class<T> type);
    
    /**
     * 캐시에서 데이터 조회 (기본값 포함)
     */
    <T> T get(String key, Class<T> type, T defaultValue);
    
    /**
     * 캐시에서 데이터 삭제
     */
    void delete(String key);
    
    /**
     * 캐시에 키가 존재하는지 확인
     */
    boolean exists(String key);
    
    /**
     * 캐시 만료 시간 설정
     */
    void expire(String key, long ttl, TimeUnit timeUnit);
    
    /**
     * 캐시 전체 삭제
     */
    void clear();
    
    /**
     * 캐시 크기 조회
     */
    long size();
    
    /**
     * 캐시 키 목록 조회
     */
    Iterable<String> keys();
    
    /**
     * 캐시 키 패턴으로 조회
     */
    Iterable<String> keys(String pattern);
} 