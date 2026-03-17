package com.famousbookshelf.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginRateLimiter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        // 允许前 5 次尝试不限速，之后每 1 分钟恢复 1 个令牌
        Bandwidth limit = Bandwidth.classic(5, Refill.intervally(1, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }

    public Bucket resolveBucket(String key) {
        return buckets.computeIfAbsent(key, k -> createNewBucket());
    }

    public boolean tryConsume(String key) {
        Bucket bucket = resolveBucket(key);
        return bucket.tryConsume(1);
    }
}
