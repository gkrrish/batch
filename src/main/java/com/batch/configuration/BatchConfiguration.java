package com.batch.configuration;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.batch.items.NewspaperItemProcessor;
import com.batch.items.NewspaperItemReader;
import com.batch.items.NewspaperItemWriter;
import com.batch.listener.NewspaperDistributionJobStatusListener;
import com.batch.listener.NewspaperDistributionProcessListener;
import com.batch.listener.NewspaperDistributionReaderListener;
import com.batch.listener.NewspaperDistributionSkipListener;
import com.batch.listener.NewspaperDistributionWriterListener;
import com.batch.model.SimpleCacheObject;
import com.batch.service.NewspaperService;
import com.batch.services.items.ProcessService;
import com.batch.services.items.ReaderService;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private NewspaperService newspaperService;
    @Autowired
    private ReaderService readerService;
    @Autowired
    private ProcessService processService;
    
    @Bean(name = "distributeJob")
    public Job distributeJob(@Qualifier("distributionStep") Step distributionStep) {
        return new JobBuilder("distributeJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(new NewspaperDistributionJobStatusListener())
                .start(distributionStep)
                .preventRestart()
                .build();
    }

    @Bean(name = "distributionStep")
    public Step distributionStep() {
        return new StepBuilder("distributionStep", jobRepository)
                .<List<SimpleCacheObject>, List<String>>chunk(1, transactionManager)
                .reader(newspaperItemReader())
                .processor(newspaperItemProcessor())
                .writer(newspaperItemWriter())
                .listener(new NewspaperDistributionReaderListener())
                .listener(new NewspaperDistributionProcessListener())
                .listener(new NewspaperDistributionWriterListener())
                .listener(new NewspaperDistributionSkipListener())
                .faultTolerant()
                .skipPolicy(new AlwaysSkipItemSkipPolicy()) // Define skip policy as needed
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<List<SimpleCacheObject>> newspaperItemReader() {
        return new NewspaperItemReader(readerService);
    }

    @Bean
    @StepScope
    public ItemProcessor<List<SimpleCacheObject>, List<String>> newspaperItemProcessor() {
        return new NewspaperItemProcessor(processService);
    }

    @Bean
    @StepScope
    public ItemWriter<List<String>> newspaperItemWriter() {
        return new NewspaperItemWriter(newspaperService);
    }
}
