package com.ddasum.core.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class CryptoUtil {
    
    private static final String AES_ALGORITHM = "AES";
    private static final String SHA256_ALGORITHM = "SHA-256";
    private static final String MD5_ALGORITHM = "MD5";
    
    /**
     * SHA-256 해시 생성
     */
    public static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(SHA256_ALGORITHM);
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 해시 생성 실패", e);
        }
    }
    
    /**
     * MD5 해시 생성
     */
    public static String md5(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(MD5_ALGORITHM);
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 해시 생성 실패", e);
        }
    }
    
    /**
     * AES 키 생성
     */
    public static SecretKey generateAESKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(AES_ALGORITHM);
            keyGen.init(256);
            return keyGen.generateKey();
        } catch (Exception e) {
            throw new RuntimeException("AES 키 생성 실패", e);
        }
    }
    
    /**
     * AES 암호화
     */
    public static String encryptAES(String plaintext, SecretKey key) {
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("AES 암호화 실패", e);
        }
    }
    
    /**
     * AES 복호화
     */
    public static String decryptAES(String encryptedText, SecretKey key) {
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("AES 복호화 실패", e);
        }
    }
    
    /**
     * Base64 인코딩
     */
    public static String encodeBase64(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }
    
    /**
     * Base64 디코딩
     */
    public static String decodeBase64(String input) {
        return new String(Base64.getDecoder().decode(input), StandardCharsets.UTF_8);
    }
    
    /**
     * 랜덤 문자열 생성
     */
    public static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return sb.toString();
    }
    
    /**
     * 랜덤 숫자 문자열 생성
     */
    public static String generateRandomNumericString(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        
        return sb.toString();
    }
    
    /**
     * 바이트 배열을 16진수 문자열로 변환
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
    
    /**
     * 16진수 문자열을 바이트 배열로 변환
     */
    public static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
    
    /**
     * 문자열을 XOR 암호화
     */
    public static String xorEncrypt(String input, String key) {
        if (StringUtil.isEmpty(input) || StringUtil.isEmpty(key)) {
            return input;
        }
        
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char inputChar = input.charAt(i);
            char keyChar = key.charAt(i % key.length());
            result.append((char) (inputChar ^ keyChar));
        }
        
        return result.toString();
    }
    
    /**
     * XOR 복호화 (암호화와 동일한 함수)
     */
    public static String xorDecrypt(String encrypted, String key) {
        return xorEncrypt(encrypted, key);
    }
    
    /**
     * 안전한 랜덤 토큰 생성
     */
    public static String generateSecureToken() {
        return sha256(generateRandomString(32) + System.currentTimeMillis());
    }
    
    /**
     * 비밀번호 해시 생성 (Salt 포함)
     */
    public static String hashPassword(String password, String salt) {
        return sha256(password + salt);
    }
    
    /**
     * Salt 생성
     */
    public static String generateSalt() {
        return generateRandomString(16);
    }
} 