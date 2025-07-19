package com.ddasum.core.config;

import com.ddasum.core.interceptor.RequestLoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    
    private final RequestLoggingInterceptor requestLoggingInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLoggingInterceptor)
                .addPathPatterns("/**")
                .addPathPatterns("http://localhost:3000/")   // 프론트엔드 프록시 포트 설정
                .excludePathPatterns("/error", "/favicon.ico");
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
} 