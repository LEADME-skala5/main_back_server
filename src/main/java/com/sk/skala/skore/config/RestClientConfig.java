package com.sk.skala.skore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Value("${server.ai.url}")
    private String aiServerUrl;

    @Bean
    public RestClient tokenRestClient() {
        return RestClient.builder()
                .baseUrl("https://login.microsoftonline.com")
                .build();
    }

    @Bean
    public RestClient graphApiRestClient() {
        return RestClient.builder()
                .baseUrl("https://graph.microsoft.com/v1.0")
                .build();
    }

    @Bean
    public RestClient aiServerRestClient() {
        return RestClient.builder()
                .baseUrl(aiServerUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}


