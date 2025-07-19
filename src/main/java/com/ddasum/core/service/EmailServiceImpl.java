package com.ddasum.core.service;

import com.ddasum.core.logging.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {
    
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    
    @Value("${spring.mail.username:no-reply@ddasum.com}")
    private String fromEmail;
    
    @Value("${spring.application.name:DDaSum}")
    private String appName;
    
    public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
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
            helper.setText(htmlContent, true); // true는 HTML 형식임을 명시
            
            // Content-Type을 명시적으로 설정
            message.setContent(htmlContent, "text/html; charset=UTF-8");
            
            mailSender.send(message);
            
            LogUtil.logBusiness("HTML 이메일 발송 완료: {} -> {}", fromEmail, to);
            
        } catch (MessagingException e) {
            log.error("HTML 이메일 발송 실패: {} -> {}", fromEmail, to, e);
            throw new RuntimeException("HTML 이메일 발송에 실패했습니다", e);
        }
    }
    
    @Override
    public void sendTemplateEmail(String to, String subject, String templateName, Map<String, Object> model) {
        try {
            log.info("템플릿 이메일 발송 시작: {} -> {}, 템플릿: {}", fromEmail, to, templateName);
            
            // Thymeleaf 컨텍스트 생성
            Context context = new Context();
            
            // 모델 데이터를 컨텍스트에 추가
            if (model != null) {
                context.setVariables(model);
                log.info("템플릿 모델 데이터: {}", model);
            }
            
            // 템플릿 처리
            String htmlContent = templateEngine.process(templateName, context);
            
            // 디버깅을 위한 로그
            log.info("생성된 HTML 내용 길이: {}", htmlContent.length());
            log.info("생성된 HTML 내용 (처음 500자): {}", 
                htmlContent.length() > 500 ? htmlContent.substring(0, 500) + "..." : htmlContent);
            
            sendHtmlEmail(to, subject, htmlContent);
            
        } catch (Exception e) {
            log.error("템플릿 이메일 발송 실패: {} -> {}, 템플릿: {}", fromEmail, to, templateName, e);
            // 템플릿 처리 실패 시 기본 텍스트 이메일로 대체
            String fallbackContent = generateTemplateContent(templateName, model);
            sendHtmlEmail(to, subject, fallbackContent);
        }
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
        
        // Thymeleaf 템플릿 엔진을 사용하여 HTML 이메일 생성
        Map<String, Object> model = new HashMap<>();
        model.put("verificationCode", verificationCode);
        
        sendTemplateEmail(to, subject, "email/verification-simple", model);
    }
    
    @Override
    public void sendPasswordResetEmail(String to, String resetToken) {
        String subject = appName + " - 비밀번호 재설정";
        
        // Thymeleaf 템플릿 엔진을 사용하여 HTML 이메일 생성
        Map<String, Object> model = new HashMap<>();
        model.put("resetLink", "https://your-domain.com/reset-password?token=" + resetToken);
        
        sendTemplateEmail(to, subject, "email/password-reset", model);
    }
    
    @Override
    public void sendWelcomeEmail(String to, String username) {
        String subject = appName + " - 환영합니다!";
        
        // Thymeleaf 템플릿 엔진을 사용하여 HTML 이메일 생성
        Map<String, Object> model = new HashMap<>();
        model.put("username", username);
        
        sendTemplateEmail(to, subject, "email/welcome", model);
    }
    
    /**
     * 템플릿 내용 생성 (fallback 구현)
     */
    private String generateTemplateContent(String templateName, Map<String, Object> model) {
        // 템플릿 이름에 따라 적절한 HTML 생성
        if (templateName.contains("verification")) {
            String verificationCode = (String) model.getOrDefault("verificationCode", "123456");
            return generateVerificationEmailHtml(verificationCode);
        } else if (templateName.contains("password-reset")) {
            String resetLink = (String) model.getOrDefault("resetLink", "#");
            return generatePasswordResetEmailHtml(resetLink);
        } else if (templateName.contains("welcome")) {
            String username = (String) model.getOrDefault("username", "사용자");
            return generateWelcomeEmailHtml(username);
        }
        
        // 기본 HTML
        StringBuilder content = new StringBuilder();
        content.append("<html><body>");
        content.append("<h1>").append(model.getOrDefault("title", "제목 없음")).append("</h1>");
        content.append("<p>").append(model.getOrDefault("content", "내용 없음")).append("</p>");
        content.append("</body></html>");
        
        return content.toString();
    }
    
    /**
     * 인증 이메일 HTML 생성
     */
    private String generateVerificationEmailHtml(String verificationCode) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>DDaSum - 이메일 인증</title>
            </head>
            <body style="font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f0f0f0;">
                <div style="max-width: 500px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
                    
                    <div style="text-align: center; margin-bottom: 30px;">
                        <h1 style="color: #333; margin-bottom: 10px;">🏡 DDaSum</h1>
                        <p style="color: #666; margin: 0;">빈집 예약/관리 플랫폼</p>
                    </div>
                    
                    <div style="text-align: center; margin-bottom: 30px;">
                        <h2 style="color: #333;">안녕하세요! 👋</h2>
                        <p style="color: #666; line-height: 1.6;">
                            DDaSum 회원가입을 위한 이메일 인증 코드를 발송해드립니다.<br>
                            아래의 인증 코드를 입력해주세요.
                        </p>
                    </div>
                    
                    <div style="background: #667eea; color: white; padding: 20px; border-radius: 10px; text-align: center; margin: 30px 0;">
                        <div style="font-size: 14px; margin-bottom: 10px;">인증 코드</div>
                        <div style="font-size: 32px; font-weight: bold; letter-spacing: 5px;">%s</div>
                    </div>
                    
                    <div style="background: #f8f9fa; padding: 15px; border-radius: 8px; margin: 20px 0; text-align: center;">
                        <p style="margin: 0; color: #666; font-size: 14px;">
                            ⏰ 이 인증 코드는 <strong>10분간</strong> 유효합니다.
                        </p>
                    </div>
                    
                    <div style="background: #fff3cd; border: 1px solid #ffeaa7; color: #856404; padding: 15px; border-radius: 8px; margin: 20px 0; text-align: center;">
                        <p style="margin: 0; font-size: 14px;">
                            ⚠️ 본인이 요청하지 않은 경우, 이 이메일을 무시하셔도 됩니다.
                        </p>
                    </div>
                    
                    <div style="text-align: center; margin-top: 30px; padding-top: 20px; border-top: 1px solid #eee;">
                        <p style="margin: 0; color: #666; font-size: 14px;">감사합니다! 🙏</p>
                        <p style="margin: 5px 0 0 0; color: #666; font-size: 14px;">DDaSum 팀 드림</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(verificationCode);
    }
    
    /**
     * 비밀번호 재설정 이메일 HTML 생성
     */
    private String generatePasswordResetEmailHtml(String resetLink) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>DDaSum - 비밀번호 재설정</title>
            </head>
            <body style="font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f0f0f0;">
                <div style="max-width: 500px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
                    
                    <div style="text-align: center; margin-bottom: 30px;">
                        <h1 style="color: #333; margin-bottom: 10px;">🏡 DDaSum</h1>
                        <p style="color: #666; margin: 0;">빈집 예약/관리 플랫폼</p>
                    </div>
                    
                    <div style="text-align: center; margin-bottom: 30px;">
                        <h2 style="color: #333;">안녕하세요! 👋</h2>
                        <p style="color: #666; line-height: 1.6;">
                            DDaSum 비밀번호 재설정을 요청하셨습니다.<br>
                            아래 버튼을 클릭하여 새로운 비밀번호를 설정해주세요.
                        </p>
                    </div>
                    
                    <div style="text-align: center; margin: 30px 0;">
                        <a href="%s" style="background: #667eea; color: white; padding: 15px 30px; border-radius: 25px; text-decoration: none; display: inline-block; font-size: 16px; font-weight: bold;">
                            🔐 비밀번호 재설정하기
                        </a>
                    </div>
                    
                    <div style="background: #f8f9fa; padding: 15px; border-radius: 8px; margin: 20px 0; text-align: center;">
                        <p style="margin: 0; color: #666; font-size: 14px;">
                            ⏰ 이 링크는 <strong>1시간간</strong> 유효합니다.
                        </p>
                    </div>
                    
                    <div style="background: #fff3cd; border: 1px solid #ffeaa7; color: #856404; padding: 15px; border-radius: 8px; margin: 20px 0; text-align: center;">
                        <p style="margin: 0; font-size: 14px;">
                            ⚠️ 본인이 요청하지 않은 경우, 이 이메일을 무시하시고<br>
                            계정 보안을 위해 비밀번호를 변경하시기 바랍니다.
                        </p>
                    </div>
                    
                    <div style="text-align: center; margin-top: 30px; padding-top: 20px; border-top: 1px solid #eee;">
                        <p style="margin: 0; color: #666; font-size: 14px;">감사합니다! 🙏</p>
                        <p style="margin: 5px 0 0 0; color: #666; font-size: 14px;">DDaSum 팀 드림</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(resetLink);
    }
    
    /**
     * 환영 이메일 HTML 생성
     */
    private String generateWelcomeEmailHtml(String username) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>DDaSum - 환영합니다!</title>
            </head>
            <body style="font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f0f0f0;">
                <div style="max-width: 500px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
                    
                    <div style="text-align: center; margin-bottom: 30px;">
                        <h1 style="color: #333; margin-bottom: 10px;">🏡 DDaSum</h1>
                        <p style="color: #666; margin: 0;">빈집 예약/관리 플랫폼</p>
                    </div>
                    
                    <div style="text-align: center; margin-bottom: 30px;">
                        <div style="font-size: 60px; margin: 20px 0;">🎉</div>
                        <h2 style="color: #333;">환영합니다, %s님! 👋</h2>
                        <p style="color: #666; line-height: 1.6;">
                            DDaSum에 가입해주셔서 정말 감사합니다!<br>
                            이제 빈집 예약과 관리를 더욱 편리하게 이용하실 수 있습니다.
                        </p>
                    </div>
                    
                    <div style="background: #f8f9fa; padding: 25px; border-radius: 15px; margin: 30px 0;">
                        <div style="margin: 15px 0; font-size: 16px; color: #333;">
                            <span style="margin-right: 15px; font-size: 20px;">🏠</span>
                            <span>빈집 등록 및 관리</span>
                        </div>
                        <div style="margin: 15px 0; font-size: 16px; color: #333;">
                            <span style="margin-right: 15px; font-size: 20px;">📅</span>
                            <span>편리한 예약 시스템</span>
                        </div>
                        <div style="margin: 15px 0; font-size: 16px; color: #333;">
                            <span style="margin-right: 15px; font-size: 20px;">⭐</span>
                            <span>리뷰 및 평가</span>
                        </div>
                        <div style="margin: 15px 0; font-size: 16px; color: #333;">
                            <span style="margin-right: 15px; font-size: 20px;">💳</span>
                            <span>안전한 결제 시스템</span>
                        </div>
                        <div style="margin: 15px 0; font-size: 16px; color: #333;">
                            <span style="margin-right: 15px; font-size: 20px;">🔒</span>
                            <span>보안된 사용자 인증</span>
                        </div>
                    </div>
                    
                    <div style="text-align: center; margin: 30px 0;">
                        <a href="#" style="background: #667eea; color: white; padding: 15px 30px; border-radius: 25px; text-decoration: none; display: inline-block; font-size: 16px; font-weight: bold;">
                            🚀 서비스 시작하기
                        </a>
                    </div>
                    
                    <div style="text-align: center; margin-top: 30px; padding-top: 20px; border-top: 1px solid #eee;">
                        <p style="margin: 0; color: #666; font-size: 14px;">감사합니다! 🙏</p>
                        <p style="margin: 5px 0 0 0; color: #666; font-size: 14px;">DDaSum 팀 드림</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(username);
    }
} 