package com.bharath.eventplatform.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class IdempotencyService {

    private final StringRedisTemplate redisTemplate;

    // How long we remember that an event was processed
    private final Duration ttl = Duration.ofHours(48); // adjust if you want

    public IdempotencyService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String key(String eventId) {
        return "processed:" + eventId;
    }

    /**
     * Returns true if the event was already processed (key exists in Redis).
     */
    public boolean isProcessed(String eventId) {
        Boolean exists = redisTemplate.hasKey(key(eventId));
        return Boolean.TRUE.equals(exists);
    }

    /**
     * Marks the event as processed with a TTL.
     */
    public void markProcessed(String eventId) {
        redisTemplate
                .opsForValue()
                .set(key(eventId), "1", ttl);
    }

    /**
     * Atomically: if not processed yet, mark as processed and return true.
     * If already processed, return false.
     *
     * This is what EventConsumerService is calling.
     */
    public boolean markIfNotProcessed(String eventId) {
        String k = key(eventId);
        Boolean already = redisTemplate.hasKey(k);
        if (Boolean.TRUE.equals(already)) {
            // already processed
            return false;
        }
        // mark as processed with TTL
        redisTemplate
                .opsForValue()
                .set(k, "1", ttl);
        return true;
    }
}
