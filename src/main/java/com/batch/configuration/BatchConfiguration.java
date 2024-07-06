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
import com.batch.listener.NewspaperJobStatusListener;
import com.batch.listener.NewspaperProcessListener;
import com.batch.listener.NewspaperReaderListener;
import com.batch.listener.NewspaperSkipListener;
import com.batch.listener.NewspaperWriterListener;
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
                .listener(new NewspaperJobStatusListener())
                .start(distributionStep)
                .preventRestart()
                .build();
    }

    @Bean(name = "distributionStep")
    public Step distributionStep() {
        return new StepBuilder("distributionStep", jobRepository)
                .<List<SimpleCacheObject>, String>chunk(1, transactionManager)
                .reader(newspaperItemReader())
                .processor(newspaperItemProcessor())
                .writer(newspaperItemWriter())
                .listener(new NewspaperReaderListener())
                .listener(new NewspaperProcessListener())
                .listener(new NewspaperWriterListener())
                .listener(new NewspaperSkipListener())
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
    public ItemProcessor<List<SimpleCacheObject>, String> newspaperItemProcessor() {
        return new NewspaperItemProcessor(processService);
    }

    @Bean
    @StepScope
    public ItemWriter<? super String> newspaperItemWriter() {
        return new NewspaperItemWriter(newspaperService);
    }
    
//    @Bean
//    public NewspaperJobExecutionListener newspaperJobExecutionListener() {
//        return new NewspaperJobExecutionListener(new NewspaperItemReader(readerService));
//    }
}
