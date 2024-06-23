package com.batch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.poc.auser.main.entity.UserSubscription;

@Service
public class BatchUserSubscriptionService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveUserSubscription(UserSubscription subscription) {
        String key = "user:subscription:" + subscription.getUserDetails().getUserid();
        redisTemplate.opsForValue().set(key, subscription);
    }

    public UserSubscription getUserSubscription(String userId) {
        String key = "user:subscription:" + userId;
        return (UserSubscription) redisTemplate.opsForValue().get(key);
    }
}
