package com.example.main_server.activityLog.graphapi;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GraphApiAccessTokenHandler {
    private static final String GRAPH_API_ACCESS_TOKEN = "graph_api_access_token";
    private static final Duration TTL = Duration.ofMinutes(60);
    private final GraphApiTokenFetcher tokenFetcher;
    private final StringRedisTemplate redisTemplate;

    public GraphApiAccessTokenHandler(
            GraphApiTokenFetcher tokenFetcher,
            StringRedisTemplate redisTemplate
    ) {
        this.tokenFetcher = tokenFetcher;
        this.redisTemplate = redisTemplate;
    }

    public String getAccessToken() {
        String token = redisTemplate.opsForValue().get(GRAPH_API_ACCESS_TOKEN);
        if (token == null) {
            token = refreshToken();
        }
        return token;
    }

    private String refreshToken() {
        String newToken = tokenFetcher.fetch();
        redisTemplate.opsForValue().set(GRAPH_API_ACCESS_TOKEN, newToken, TTL);
        log.info("Access token refreshed and stored in Redis with TTL {} minutes", TTL.toMinutes());
        return newToken;
    }
}
