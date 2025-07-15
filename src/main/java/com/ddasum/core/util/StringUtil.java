package com.ddasum.core.util;

import java.util.UUID;
import java.util.regex.Pattern;

public class StringUtil {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^01[016789]-?\\d{3,4}-?\\d{4}$"
    );
    
    /**
     * 문자열이 null이거나 빈 문자열인지 확인
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * 문자열이 null이 아니고 빈 문자열이 아닌지 확인
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    /**
     * 문자열을 대문자로 변환
     */
    public static String toUpperCase(String str) {
        return str != null ? str.toUpperCase() : null;
    }
    
    /**
     * 문자열을 소문자로 변환
     */
    public static String toLowerCase(String str) {
        return str != null ? str.toLowerCase() : null;
    }
    
    /**
     * 문자열의 첫 글자를 대문자로 변환
     */
    public static String capitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
    
    /**
     * 문자열에서 특정 문자 제거
     */
    public static String removeChar(String str, char ch) {
        if (isEmpty(str)) {
            return str;
        }
        return str.replace(String.valueOf(ch), "");
    }
    
    /**
     * 문자열에서 공백 제거
     */
    public static String removeWhitespace(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.replaceAll("\\s", "");
    }
    
    /**
     * 문자열을 지정된 길이로 자르기
     */
    public static String truncate(String str, int maxLength) {
        if (isEmpty(str) || maxLength <= 0) {
            return str;
        }
        return str.length() <= maxLength ? str : str.substring(0, maxLength);
    }
    
    /**
     * 문자열을 지정된 길이로 자르고 말줄임표 추가
     */
    public static String truncateWithEllipsis(String str, int maxLength) {
        if (isEmpty(str) || maxLength <= 3) {
            return str;
        }
        return str.length() <= maxLength ? str : str.substring(0, maxLength - 3) + "...";
    }
    
    /**
     * 문자열이 이메일 형식인지 확인
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * 문자열이 전화번호 형식인지 확인
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }
    
    /**
     * 문자열이 숫자로만 구성되어 있는지 확인
     */
    public static boolean isNumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return str.matches("\\d+");
    }
    
    /**
     * 문자열이 알파벳으로만 구성되어 있는지 확인
     */
    public static boolean isAlpha(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return str.matches("[a-zA-Z]+");
    }
    
    /**
     * 문자열이 알파벳과 숫자로만 구성되어 있는지 확인
     */
    public static boolean isAlphanumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return str.matches("[a-zA-Z0-9]+");
    }
    
    /**
     * UUID 생성
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * 하이픈이 없는 UUID 생성
     */
    public static String generateUUIDWithoutHyphens() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    /**
     * 문자열을 카멜케이스로 변환
     */
    public static String toCamelCase(String str) {
        if (isEmpty(str)) {
            return str;
        }
        
        String[] words = str.toLowerCase().split("[\\s_-]+");
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < words.length; i++) {
            if (i == 0) {
                result.append(words[i]);
            } else {
                result.append(capitalize(words[i]));
            }
        }
        
        return result.toString();
    }
    
    /**
     * 문자열을 스네이크케이스로 변환
     */
    public static String toSnakeCase(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
    
    /**
     * 문자열을 케밥케이스로 변환
     */
    public static String toKebabCase(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.replaceAll("([a-z])([A-Z])", "$1-$2").toLowerCase();
    }
    
    /**
     * 문자열에서 특정 패턴을 다른 문자열로 치환
     */
    public static String replacePattern(String str, String pattern, String replacement) {
        if (isEmpty(str) || isEmpty(pattern)) {
            return str;
        }
        return str.replaceAll(pattern, replacement);
    }
    
    /**
     * 문자열을 지정된 구분자로 분할
     */
    public static String[] split(String str, String delimiter) {
        if (isEmpty(str)) {
            return new String[0];
        }
        return str.split(delimiter);
    }
    
    /**
     * 문자열 배열을 하나의 문자열로 결합
     */
    public static String join(String[] array, String delimiter) {
        if (array == null || array.length == 0) {
            return "";
        }
        
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                result.append(delimiter);
            }
            result.append(array[i]);
        }
        
        return result.toString();
    }
} 