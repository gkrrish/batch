package com.batch.writer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.batch.service.BatchUserSubscriptionService;
import com.poc.auser.main.entity.UserSubscription;

@Component
public class UserSubscriptionItemWriter implements ItemWriter<UserSubscription> {

    @Autowired
    private BatchUserSubscriptionService batchUserSubscriptionService;

    @Override
    public void write(Chunk<? extends UserSubscription> chunk) throws Exception {
        for (UserSubscription subscription : chunk.getItems()) {
            batchUserSubscriptionService.saveUserSubscription(subscription);
        }
    }
}
