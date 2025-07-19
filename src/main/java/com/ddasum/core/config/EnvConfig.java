package com.ddasum.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class EnvConfig {
    // spring-dotenv 라이브러리가 자동으로 .env 탐색 후 환경 변수를 Properties에 변수 적용
} 