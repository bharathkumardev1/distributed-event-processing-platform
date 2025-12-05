package com.bharath.eventplatform.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class IdempotencyService {

    private final StringRedisTemplate redisTemplate;
    private final Duration ttl = Duration.ofHours(48); // make configurable later

    public IdempotencyService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String key(String eventId) {
        return "processed:" + eventId;
    }

    public boolean isProcessed(String eventId) {
        Boolean exists = redisTemplate.hasKey(key(eventId));
        return Boolean.TRUE.equals(exists);
    }

    public void markProcessed(String eventId) {
        redisTemplate.opsForValue().set(key(eventId), "1", ttl);
    }
}
