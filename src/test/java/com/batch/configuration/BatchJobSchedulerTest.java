package com.batch.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.batch.util.BatchUTIL;

@SpringBootTest
@EnableScheduling
class BatchJobSchedulerTest {

    @MockBean
    private JobLauncher jobLauncher;

    @MockBean
    @Qualifier("distributeJob")
    private Job distributeJob;

    @Autowired
    private BatchJobScheduler batchJobScheduler;

    @Mock
    private JobExecution jobExecution;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(jobExecution.getStatus()).thenReturn(null);
    }

    @Test
    void testRunProcessJob_Success() throws Exception {
        when(jobLauncher.run(any(Job.class), any(JobParameters.class))).thenReturn(jobExecution);
        batchJobScheduler.runProcessJob();
        verify(jobLauncher, times(1)).run(any(Job.class), any(JobParameters.class));
    }

    @Test
    void testRunProcessJob_Failure() throws Exception {
        when(jobLauncher.run(any(Job.class), any(JobParameters.class))).thenThrow(new RuntimeException("Job execution failed"));
        try {
            batchJobScheduler.runProcessJob();
        } catch (Exception e) {
            assertEquals("Job execution failed", e.getMessage());
        }
        verify(jobLauncher, times(1)).run(any(Job.class), any(JobParameters.class));
    }

    @Test
    void testPreventReaderRepetitive() {
        BatchUTIL.setFlag(true);
        batchJobScheduler.preventReaderRepetative();
        assertEquals(false, BatchUTIL.getFlag());
    }
}
