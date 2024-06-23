package com.batch.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.batch.listner.BatchJobCompletionListener;
import com.batch.processor.UserSubscriptionItemProcessor;
import com.batch.reader.UserSubscriptionItemReader;
import com.batch.writer.UserSubscriptionItemWriter;
import com.poc.auser.main.entity.UserSubscription;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private UserSubscriptionItemReader reader;

    @Autowired
    private UserSubscriptionItemProcessor processor;

    @Autowired
    private UserSubscriptionItemWriter writer;

    @Autowired
    private BatchJobCompletionListener listener;

    @Bean
    public Job job() {
        JobBuilder jobBuilder = new JobBuilder("job", jobRepository);
        return jobBuilder.incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step())
                .end()
                .build();
    }

    @Bean
    public Step step() {
        StepBuilder stepBuilder = new StepBuilder("step", jobRepository);
        return stepBuilder.<UserSubscription, UserSubscription>chunk(10, transactionManager)
                .reader(reader.reader())
                .processor(processor)
                .writer(writer)
                .build();
    }
}
