package com.ddasum.core.service;

import java.util.Map;

public interface EmailService {
    
    /**
     * 단순 텍스트 이메일 발송
     */
    void sendSimpleEmail(String to, String subject, String content);
    
    /**
     * HTML 이메일 발송
     */
    void sendHtmlEmail(String to, String subject, String htmlContent);
    
    /**
     * 템플릿 기반 이메일 발송
     */
    void sendTemplateEmail(String to, String subject, String templateName, Map<String, Object> model);
    
    /**
     * 첨부파일이 있는 이메일 발송
     */
    void sendEmailWithAttachment(String to, String subject, String content, String attachmentPath);
    
    /**
     * 인증 이메일 발송
     */
    void sendVerificationEmail(String to, String verificationCode);
    
    /**
     * 비밀번호 재설정 이메일 발송
     */
    void sendPasswordResetEmail(String to, String resetToken);
    
    /**
     * 환영 이메일 발송
     */
    void sendWelcomeEmail(String to, String username);
} 