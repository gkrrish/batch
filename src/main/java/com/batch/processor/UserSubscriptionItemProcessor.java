package com.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.poc.auser.main.entity.UserSubscription;

@Component
public class UserSubscriptionItemProcessor implements ItemProcessor<UserSubscription, UserSubscription> {

    @Override
    public UserSubscription process(UserSubscription subscription) {
        // Add processing logic here if needed
        return subscription;
    }
}
