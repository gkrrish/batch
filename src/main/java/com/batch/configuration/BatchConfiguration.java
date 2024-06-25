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

import com.batch.listener.JobProcessListener;
import com.batch.listener.JobReaderListener;
import com.batch.listener.JobSkipListener;
import com.batch.listener.JobStatusListener;
import com.batch.listener.JobWriterListener;
import com.batch.process.NewspaperItemProcessor;
import com.batch.reader.NewspaperItemReader;
import com.batch.service.NewspaperService;
import com.batch.writer.NewspaperItemWriter;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Autowired
    private NewspaperService newspaperService;

    @Autowired
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
                .reader(newspaperItemReader())
                .processor(newspaperItemProcessor())
                .writer(newspaperItemWriter())
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
    public ItemReader<List<String>> newspaperItemReader() {
        return new NewspaperItemReader(newspaperService);
    }

    @Bean
    @StepScope
    public ItemProcessor<List<String>, List<String>> newspaperItemProcessor() {
        return new NewspaperItemProcessor();
    }

    @Bean
    @StepScope
    public ItemWriter<List<String>> newspaperItemWriter() {
        return new NewspaperItemWriter(newspaperService);
    }
}
