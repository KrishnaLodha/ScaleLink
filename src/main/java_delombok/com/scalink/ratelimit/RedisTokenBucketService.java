package com.scalink.ratelimit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Distributed token-bucket rate limiter backed by Redis.
 * Works consistently across multiple application instances behind a load balancer.
 */
@Slf4j
@Service
@Profile("!test")
@RequiredArgsConstructor
public class RedisTokenBucketService {

    private static final String TOKEN_BUCKET_SCRIPT = """
            local key = KEYS[1]
            local capacity = tonumber(ARGV[1])
            local refill_rate = tonumber(ARGV[2])
            local now = tonumber(ARGV[3])
            local requested = tonumber(ARGV[4])
            local ttl = tonumber(ARGV[5])

            local data = redis.call('HMGET', key, 'tokens', 'timestamp')
            local tokens = tonumber(data[1])
            local last_refill = tonumber(data[2])

            if tokens == nil then
                tokens = capacity
                last_refill = now
            end

            local delta = math.max(0, now - last_refill)
            tokens = math.min(capacity, tokens + delta * refill_rate)

            if tokens < requested then
                return 0
            end

            tokens = tokens - requested
            redis.call('HMSET', key, 'tokens', tokens, 'timestamp', now)
            redis.call('EXPIRE', key, ttl)
            return 1
            """;

    private final StringRedisTemplate stringRedisTemplate;

    private final DefaultRedisScript<Long> tokenBucketScript =
            new DefaultRedisScript<>(TOKEN_BUCKET_SCRIPT, Long.class);

    public boolean tryConsume(String key, int capacity, int windowSeconds) {
        double refillRate = (double) capacity / windowSeconds;
        long now = System.currentTimeMillis() / 1000;

        Long allowed = stringRedisTemplate.execute(
                tokenBucketScript,
                Collections.singletonList(key),
                String.valueOf(capacity),
                String.valueOf(refillRate),
                String.valueOf(now),
                "1",
                String.valueOf(windowSeconds * 2));

        return allowed != null && allowed == 1L;
    }
}
