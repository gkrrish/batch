package com.batch.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
@SpringBatchTest
@SpringBootTest
class BatchConfigurationTest {

    @Autowired
    private JobLauncher jobLauncher;

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

    @Autowired
    private Job distributeJob;

    @Autowired
    private Step distributionStep;

    @BeforeEach
    void setUp() {
        // Perform any necessary setup here
    }

    @Test
    void testDistributeJob_Success() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        JobExecution jobExecution = jobLauncher.run(distributeJob, jobParameters);

        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
    }

    @Test
    void testDistributeJob_Failure() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        // Mock reader to throw an exception
        Step faultyStep = new StepBuilder("faultyStep", jobRepository)
                .<List<SimpleCacheObject>, String>chunk(1, transactionManager)
                .reader(() -> {
                    throw new RuntimeException("Simulated reader failure");
                })
                .processor(newspaperItemProcessor())
                .writer(newspaperItemWriter())
                .listener(new NewspaperReaderListener())
                .listener(new NewspaperProcessListener())
                .listener(new NewspaperWriterListener())
                .listener(new NewspaperSkipListener())
                .faultTolerant()
                .skipPolicy(new AlwaysSkipItemSkipPolicy())
                .build();

        Job faultyJob = new JobBuilder("faultyJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(new NewspaperJobStatusListener())
                .start(faultyStep)
                .preventRestart()
                .build();

        JobExecution jobExecution = jobLauncher.run(faultyJob, jobParameters);

        assertEquals(BatchStatus.FAILED, jobExecution.getStatus());
    }

    @Test
    void testDistributionStepConfiguration() {
        assertNotNull(distributionStep);
        assertEquals("distributionStep", distributionStep.getName());
    }

    @Bean
    @StepScope
    public NewspaperItemReader newspaperItemReader() {
        return new NewspaperItemReader(readerService);
    }

    @Bean
    @StepScope
    public NewspaperItemProcessor newspaperItemProcessor() {
        return new NewspaperItemProcessor(processService);
    }

    @Bean
    @StepScope
    public NewspaperItemWriter newspaperItemWriter() {
        return new NewspaperItemWriter(newspaperService);
    }
}
