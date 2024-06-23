package com.batch.redis.cache.job;

import com.batch.redis.cache.service.RedisCacheService;
import com.poc.auser.main.entity.UserSubscription;
import com.poc.auser.main.repository.UserSubscriptionRepository;
import com.poc.auser.master.entity.BatchJob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSubscriptionCacheScheduler {

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    @Scheduled(cron = "0 0 23 30 * ?")
    public void cacheUserSubscriptions() {
        List<UserSubscription> subscriptions = userSubscriptionRepository.findAll();
        for (UserSubscription subscription : subscriptions) {
            String key = "user:subscription:" + subscription.getId().getUser().getUserid();
            redisCacheService.cacheUserSubscription(key, subscription);
        }
    }
}
