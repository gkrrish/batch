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
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.batch.listner.JobProcessListener;
import com.batch.listner.JobReaderListener;
import com.batch.listner.JobSkipListener;
import com.batch.listner.JobStatusListener;
import com.batch.listner.JobWriterListener;
import com.batch.process.NewspaperItemProcessor;
import com.batch.reader.NewspaperItemReader;
import com.batch.service.NewspaperService;
import com.batch.writer.NewspaperItemWriter;

@Configuration
@EnableBatchProcessing
@SuppressWarnings("unchecked")
public class BatchConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Autowired
    private NewspaperService newspaperService;

    public BatchConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean(name = "distributeJob")
    public Job distributeJob(@Qualifier("distributionStep") Step distributionStep) {
        return new JobBuilder("distributeJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(new JobStatusListener())
                .start(distributionStep)
                .preventRestart()
                .build();
    }

    @Bean(name = "distributionStep")
    public Step distributionStep() {
        return new StepBuilder("distributionStep", jobRepository)
                .<List<String>, List<String>>chunk(1, transactionManager)
                .reader((ItemReader<? extends List<String>>) newspaperItemReader())
                .processor((ItemProcessor<? super List<String>, ? extends List<String>>) newspaperItemProcessor())
                .writer((ItemWriter<? super List<String>>) newspaperItemWriter())
                .listener(new JobReaderListener())
                .listener(new JobProcessListener())
                .listener(new JobWriterListener())
                .listener(new JobSkipListener())
                .faultTolerant()
                .skipPolicy(null) // Define skip policy as needed
                .build();
    }

    
    @Bean
    @StepScope
    public NewspaperItemReader newspaperItemReader() {
        return new NewspaperItemReader(newspaperService);
    }

    @Bean
    @StepScope
    public NewspaperItemProcessor newspaperItemProcessor() {
        return new NewspaperItemProcessor();
    }

    @Bean
    @StepScope
    public NewspaperItemWriter newspaperItemWriter() {
        return new NewspaperItemWriter(newspaperService);
    }
}
