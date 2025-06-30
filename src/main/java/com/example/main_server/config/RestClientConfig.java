package com.example.main_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
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
}


