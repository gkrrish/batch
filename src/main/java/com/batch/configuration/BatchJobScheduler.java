package com.batch.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BatchJobScheduler {
	private static final Logger log = LoggerFactory.getLogger(BatchJobScheduler.class);

	
	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	@Qualifier("distributeJob")
	private Job distributeJob;

	@Scheduled(cron = "0 0/1 * * * *")//every one minutes
	public void runProcessJob() throws Exception {
		JobParameters jobParameters=new JobParametersBuilder()
										.addLong("dateTime", System.currentTimeMillis())
										.toJobParameters();
		JobExecution execution = jobLauncher.run(distributeJob, jobParameters);
		log.info("Job Execution Status: {}", execution.getStatus());
	}
    
}
