package com.ddasum.core.config;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource sc = new UrlBasedCorsConfigurationSource();
        CorsConfiguration cf = new CorsConfiguration();
        cf.setAllowCredentials(true);
        cf.addAllowedHeader("*");
        cf.addAllowedOriginPattern("*");
        cf.addAllowedMethod("*");
        sc.registerCorsConfiguration("/api/v1/**", cf);

        return new CorsFilter();
    }
}
