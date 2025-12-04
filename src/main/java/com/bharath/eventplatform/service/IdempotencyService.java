package com.bharath.eventplatform.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class IdempotencyService {

    private final StringRedisTemplate redisTemplate;
    private final Duration ttl;

    public IdempotencyService(
            StringRedisTemplate redisTemplate,
            @Value("${app.idempotency.ttl-seconds}") long ttlSeconds) {
        this.redisTemplate = redisTemplate;
        this.ttl = Duration.ofSeconds(ttlSeconds);
    }

    public boolean markIfNotProcessed(String eventId) {
        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(buildKey(eventId), "processed", ttl);
        return Boolean.TRUE.equals(success);
    }

    private String buildKey(String eventId) {
        return "event:processed:" + eventId;
    }
}
