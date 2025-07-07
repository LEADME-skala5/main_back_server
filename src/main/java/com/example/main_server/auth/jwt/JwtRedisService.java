package com.example.main_server.auth.jwt;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * JWT 리프레시 토큰을 Redis에 관리하는 서비스 클래스입니다.
 * <p>
 * 사용자 인증에 필요한 리프레시 토큰을 Redis에 저장, 조회, 삭제, 검증하는 기능을 제공합니다. 토큰의 안전한 관리와 만료 처리를 담당합니다.
 * <p>
 * 주요 기능: - 리프레시 토큰 저장 - 리프레시 토큰 조회 - 리프레시 토큰 삭제 - 리프레시 토큰 유효성 검증
 */


@Slf4j
@Service
@RequiredArgsConstructor
public class JwtRedisService {
    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";
    private final RedisTemplate<String, String> redisTemplate;

    public void saveRefreshToken(Long userId, String refreshToken, long expirationMs) {
        String key = REFRESH_TOKEN_PREFIX + userId;
        redisTemplate.opsForValue().set(key, refreshToken, Duration.ofMillis(expirationMs));
        log.info("리프레시 토큰이 Redis에 저장되었습니다. 사용자 ID: {}", userId);
    }

    public String getRefreshToken(Long userId) {
        String key = REFRESH_TOKEN_PREFIX + userId;
        String token = redisTemplate.opsForValue().get(key);
        if (token == null) {
            log.info("Redis에서 리프레시 토큰을 찾을 수 없습니다. 사용자 ID: {}", userId);
        }
        return token;
    }

    public void deleteRefreshToken(Long userId) {
        String key = REFRESH_TOKEN_PREFIX + userId;
        Boolean deleted = redisTemplate.delete(key);
        if (deleted) {
            log.info("Redis에서 리프레시 토큰이 삭제되었습니다. 사용자 ID: {}", userId);
        } else {
            log.warn("Redis에서 리프레시 토큰 삭제 실패. 사용자 ID: {}", userId);
        }
    }

    public boolean validateRefreshToken(Long userId, String refreshToken) {
        String storedToken = getRefreshToken(userId);
        return refreshToken != null && refreshToken.equals(storedToken);
    }
}
