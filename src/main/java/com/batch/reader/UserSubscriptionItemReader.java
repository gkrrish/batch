package com.batch.reader;

import com.poc.auser.main.entity.UserSubscription;
import com.poc.auser.main.repository.UserSubscriptionRepository;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@Configuration
public class UserSubscriptionItemReader {

    private final UserSubscriptionRepository userSubscriptionRepository;

    public UserSubscriptionItemReader(UserSubscriptionRepository userSubscriptionRepository) {
        this.userSubscriptionRepository = userSubscriptionRepository;
    }

    @Bean
    @StepScope
    public RepositoryItemReader<UserSubscription> reader() {
        RepositoryItemReader<UserSubscription> reader = new RepositoryItemReader<>();
        reader.setRepository(userSubscriptionRepository);
        reader.setMethodName("findAll");
        reader.setPageSize(10);
        reader.setSort(Collections.singletonMap("userId", Sort.Direction.ASC));
        return reader;
    }
}
