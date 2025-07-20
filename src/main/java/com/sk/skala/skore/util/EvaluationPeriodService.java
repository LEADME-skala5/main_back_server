package com.sk.skala.skore.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 평가 기간 정보를 관리하는 서비스 클래스입니다.
 * <p>
 * Redis에 저장된 현재 평가 연도와 분기 정보를 조회하는 기능을 제공합니다. 시스템 전반에서 현재 평가 기간을 일관되게 사용할 수 있도록 중앙화된 접근점을 제공합니다.
 * <p>
 * 주요 기능: - 현재 평가 연도 조회 - 현재 평가 분기 조회
 */


@Service
@RequiredArgsConstructor
public class EvaluationPeriodService {
    private static final String REDIS_KEY = "evaluation:current";
    private final RedisTemplate<String, String> redisTemplate;

    public int getCurrentYear() {
        String year = (String) redisTemplate.opsForHash().get(REDIS_KEY, "year");
        if (year == null) {
            throw new IllegalStateException("Redis에 'year' 값이 존재하지 않습니다.");
        }
        return Integer.parseInt(year);
    }

    public int getCurrentQuarter() {
        String quarter = (String) redisTemplate.opsForHash().get(REDIS_KEY, "quarter");
        if (quarter == null) {
            throw new IllegalStateException("Redis에 'quarter' 값이 존재하지 않습니다.");
        }
        return Integer.parseInt(quarter);
    }
}
