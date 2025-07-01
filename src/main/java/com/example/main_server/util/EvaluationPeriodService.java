package com.example.main_server.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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
