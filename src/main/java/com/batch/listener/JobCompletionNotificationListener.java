package com.batch.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        // Custom logic before job starts, if needed
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        // Custom logic after job completion
    }
}
