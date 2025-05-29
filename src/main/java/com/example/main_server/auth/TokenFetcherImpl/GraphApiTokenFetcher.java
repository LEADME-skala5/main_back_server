package com.example.main_server.auth.TokenFetcherImpl;

import com.example.main_server.auth.TokenFetcher;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Component
@Qualifier("graph")
public class GraphApiTokenFetcher implements TokenFetcher {
    private static final String ACCESS_TOKEN_KEY = "access_token";
    private final WebClient webClient;

    @Value("${graph.api.tenant-id}")
    private String tenantId;
    @Value("${graph.api.client-id}")
    private String clientId;
    @Value("${graph.api.client-secret}")
    private String clientSecret;
    @Value("${graph.api.scope}")
    private String scope;

    public GraphApiTokenFetcher(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://login.microsoftonline.com").build();
    }

    @Override
    public String fetch() {
        String body = "client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&scope=" + scope +
                "&grant_type=client_credentials";

        try {
            JsonNode response = webClient.post()
                    .uri("/{tenant_id}/oauth2/v2.0/token", tenantId)
                    .bodyValue(body)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            if (response == null || response.get(ACCESS_TOKEN_KEY) == null) {
                throw new IllegalStateException("No access_token in response");
            }

            return response.get(ACCESS_TOKEN_KEY).asText();

        } catch (WebClientResponseException e) {
            log.error("HTTP error while fetching token: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch access token: HTTP error", e);
        } catch (Exception e) {
            log.error("Unexpected error while fetching token", e);
            throw new RuntimeException("Failed to fetch access token", e);
        }
    }
}
