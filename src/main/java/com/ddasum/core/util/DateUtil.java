package com.ddasum.core.util;

import com.ddasum.core.constants.CommonConstants;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtil {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(CommonConstants.DATE_FORMAT);
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(CommonConstants.DATETIME_FORMAT);
    
    /**
     * 현재 날짜를 문자열로 반환
     */
    public static String getCurrentDateString() {
        return LocalDate.now().format(DATE_FORMATTER);
    }
    
    /**
     * 현재 날짜시간을 문자열로 반환
     */
    public static String getCurrentDateTimeString() {
        return LocalDateTime.now().format(DATETIME_FORMATTER);
    }
    
    /**
     * LocalDateTime을 문자열로 변환
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATETIME_FORMATTER) : null;
    }
    
    /**
     * LocalDate를 문자열로 변환
     */
    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : null;
    }
    
    /**
     * 문자열을 LocalDateTime으로 변환
     */
    public static LocalDateTime parseDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, DATETIME_FORMATTER);
    }
    
    /**
     * 문자열을 LocalDate로 변환
     */
    public static LocalDate parseDate(String dateString) {
        return LocalDate.parse(dateString, DATE_FORMATTER);
    }
    
    /**
     * 두 날짜 사이의 일수 계산
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
    
    /**
     * 두 날짜시간 사이의 시간 계산 (시간 단위)
     */
    public static long hoursBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return ChronoUnit.HOURS.between(startDateTime, endDateTime);
    }
    
    /**
     * 두 날짜시간 사이의 분 계산
     */
    public static long minutesBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return ChronoUnit.MINUTES.between(startDateTime, endDateTime);
    }
    
    /**
     * Date를 LocalDateTime으로 변환
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return date != null ? date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime() : null;
    }
    
    /**
     * LocalDateTime을 Date로 변환
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return localDateTime != null ? 
            Date.from(localDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant()) : null;
    }
    
    /**
     * 현재 시간이 특정 시간 범위 내에 있는지 확인
     */
    public static boolean isWithinTimeRange(LocalDateTime target, LocalDateTime start, LocalDateTime end) {
        return !target.isBefore(start) && !target.isAfter(end);
    }
    
    /**
     * 날짜가 유효한지 확인
     */
    public static boolean isValidDate(String dateString) {
        try {
            LocalDate.parse(dateString, DATE_FORMATTER);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 날짜시간이 유효한지 확인
     */
    public static boolean isValidDateTime(String dateTimeString) {
        try {
            LocalDateTime.parse(dateTimeString, DATETIME_FORMATTER);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
} 