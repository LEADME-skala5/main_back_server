package com.example.main_server;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 API 경로
                .allowedOrigins("http://localhost:3000") // 프론트엔드 URL 허용
                .allowedMethods("*") // 모든 HTTP 메서드 허용
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}