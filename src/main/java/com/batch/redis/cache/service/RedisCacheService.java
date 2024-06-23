package com.batch.redis.cache.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisCacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void cacheUserSubscription(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Object getUserSubscription(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
