package com.example.main_server.auth.jwt;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtRedisService {
    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";
    private final RedisTemplate<String, String> redisTemplate;

    // Redis에 리프레시 토큰 저장
    public void saveRefreshToken(Long userId, String refreshToken, long expirationMs) {
        String key = REFRESH_TOKEN_PREFIX + userId;
        redisTemplate.opsForValue().set(key, refreshToken, Duration.ofMillis(expirationMs));
        log.info("리프레시 토큰이 Redis에 저장되었습니다. 사용자 ID: {}", userId);
    }

    // Redis에서 리프레시 토큰 조회
    public String getRefreshToken(Long userId) {
        String key = REFRESH_TOKEN_PREFIX + userId;
        String token = redisTemplate.opsForValue().get(key);
        if (token == null) {
            log.info("Redis에서 리프레시 토큰을 찾을 수 없습니다. 사용자 ID: {}", userId);
        }
        return token;
    }

    // Redis에서 리프레시 토큰 삭제 (로그아웃)
    public void deleteRefreshToken(Long userId) {
        String key = REFRESH_TOKEN_PREFIX + userId;
        Boolean deleted = redisTemplate.delete(key);
        if (Boolean.TRUE.equals(deleted)) {
            log.info("Redis에서 리프레시 토큰이 삭제되었습니다. 사용자 ID: {}", userId);
        } else {
            log.warn("Redis에서 리프레시 토큰 삭제 실패. 사용자 ID: {}", userId);
        }
    }

    // 리프레시 토큰 유효성 검증
    public boolean validateRefreshToken(Long userId, String refreshToken) {
        String storedToken = getRefreshToken(userId);
        return refreshToken != null && refreshToken.equals(storedToken);
    }
}
