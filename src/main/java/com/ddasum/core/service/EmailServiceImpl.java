package com.ddasum.core.service;

import com.ddasum.core.logging.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {
    
    private final JavaMailSender mailSender;
    
    @Value("${spring.mail.username:no-reply@ddasum.com}")
    private String fromEmail;
    
    @Value("${spring.application.name:DDaSum}")
    private String appName;
    
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    @Override
    public void sendSimpleEmail(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            
            mailSender.send(message);
            
            LogUtil.logBusiness("이메일 발송 완료: {} -> {}", fromEmail, to);
            
        } catch (Exception e) {
            log.error("이메일 발송 실패: {} -> {}", fromEmail, to, e);
            throw new RuntimeException("이메일 발송에 실패했습니다", e);
        }
    }
    
    @Override
    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            
            LogUtil.logBusiness("HTML 이메일 발송 완료: {} -> {}", fromEmail, to);
            
        } catch (MessagingException e) {
            log.error("HTML 이메일 발송 실패: {} -> {}", fromEmail, to, e);
            throw new RuntimeException("HTML 이메일 발송에 실패했습니다", e);
        }
    }
    
    @Override
    public void sendTemplateEmail(String to, String subject, String templateName, Map<String, Object> model) {
        // TODO: Thymeleaf 템플릿 엔진을 사용한 템플릿 이메일 구현
        String content = generateTemplateContent(templateName, model);
        sendHtmlEmail(to, subject, content);
    }
    
    @Override
    public void sendEmailWithAttachment(String to, String subject, String content, String attachmentPath) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content);
            
            // 첨부파일 추가
            helper.addAttachment("attachment", new java.io.File(attachmentPath));
            
            mailSender.send(message);
            
            LogUtil.logBusiness("첨부파일 이메일 발송 완료: {} -> {}", fromEmail, to);
            
        } catch (MessagingException e) {
            log.error("첨부파일 이메일 발송 실패: {} -> {}", fromEmail, to, e);
            throw new RuntimeException("첨부파일 이메일 발송에 실패했습니다", e);
        }
    }
    
    @Override
    public void sendVerificationEmail(String to, String verificationCode) {
        String subject = appName + " - 이메일 인증";
        String content = String.format(
            "안녕하세요!\n\n" +
            "이메일 인증을 위해 아래 코드를 입력해주세요:\n\n" +
            "인증 코드: %s\n\n" +
            "이 코드는 10분간 유효합니다.\n\n" +
            "감사합니다,\n" +
            "%s 팀", verificationCode, appName
        );
        
        sendSimpleEmail(to, subject, content);
    }
    
    @Override
    public void sendPasswordResetEmail(String to, String resetToken) {
        String subject = appName + " - 비밀번호 재설정";
        String content = String.format(
            "안녕하세요!\n\n" +
            "비밀번호 재설정을 요청하셨습니다.\n\n" +
            "아래 링크를 클릭하여 비밀번호를 재설정하세요:\n\n" +
            "재설정 링크: https://your-domain.com/reset-password?token=%s\n\n" +
            "이 링크는 1시간간 유효합니다.\n\n" +
            "감사합니다,\n" +
            "%s 팀", resetToken, appName
        );
        
        sendSimpleEmail(to, subject, content);
    }
    
    @Override
    public void sendWelcomeEmail(String to, String username) {
        String subject = appName + " - 환영합니다!";
        String content = String.format(
            "안녕하세요 %s님!\n\n" +
            "%s에 가입해주셔서 감사합니다.\n\n" +
            "앞으로 좋은 서비스로 보답하겠습니다.\n\n" +
            "감사합니다,\n" +
            "%s 팀", username, appName, appName
        );
        
        sendSimpleEmail(to, subject, content);
    }
    
    /**
     * 템플릿 내용 생성 (임시 구현)
     */
    private String generateTemplateContent(String templateName, Map<String, Object> model) {
        // TODO: 실제 템플릿 엔진 사용
        StringBuilder content = new StringBuilder();
        content.append("<html><body>");
        content.append("<h1>").append(model.getOrDefault("title", "제목 없음")).append("</h1>");
        content.append("<p>").append(model.getOrDefault("content", "내용 없음")).append("</p>");
        content.append("</body></html>");
        
        return content.toString();
    }
} 