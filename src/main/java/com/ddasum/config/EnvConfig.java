package com.ddasum.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class EnvConfig {
    // spring-dotenv 라이브러리가 자동으로 .env 파일을 로드합니다
} 