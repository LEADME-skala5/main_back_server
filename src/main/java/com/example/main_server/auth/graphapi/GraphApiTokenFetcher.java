package com.example.main_server.auth.graphapi;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class GraphApiTokenFetcher {
    private static final String ACCESS_TOKEN_KEY = "access_token";
    private final RestClient tokenRestClient;

    @Value("${graph.api.tenant-id}")
    private String tenantId;
    @Value("${graph.api.client-id}")
    private String clientId;
    @Value("${graph.api.client-secret}")
    private String clientSecret;
    @Value("${graph.api.scope}")
    private String scope;

    public String fetch() {
        String body = "client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&scope=" + scope +
                "&grant_type=client_credentials";

        try {
            JsonNode response = tokenRestClient.post()
                    .uri("/{tenant_id}/oauth2/v2.0/token", tenantId)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .body(body)
                    .retrieve()
                    .body(JsonNode.class);

            if (response == null || response.get(ACCESS_TOKEN_KEY) == null) {
                throw new IllegalStateException("No access_token in response");
            }

            return response.get(ACCESS_TOKEN_KEY).asText();

        } catch (HttpClientErrorException e) {
            log.error("HTTP error while fetching token: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch access token: HTTP error", e);
        } catch (Exception e) {
            log.error("Unexpected error while fetching token", e);
            throw new RuntimeException("Failed to fetch access token", e);
        }
    }
}
