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

import com.batch.util.BatchUTIL;

@Component
public class BatchJobScheduler {
	private static final Logger log = LoggerFactory.getLogger(BatchJobScheduler.class);

	
	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	@Qualifier("distributeJob")
	private Job distributeJob;

	@Scheduled(cron = "30 28,58 * * * *") // Every hour 28:30 and 58:30 
	public void runProcessJob() throws Exception {
		JobParameters jobParameters=new JobParametersBuilder()
										.addLong("dateTime", System.currentTimeMillis())
										.toJobParameters();
		JobExecution execution = jobLauncher.run(distributeJob, jobParameters);
		log.info("Job Execution Status: {}", execution.getStatus());
	}
	
	/**
	 * This is always a 30 seconds lesser than runProcessJob
	 */
	@Scheduled(cron = "0 28,58 * * * *") // Every hour 28:00 and 58:00
	public void preventReaderRepetative() {
		log.info("Updated the prevent Reader repeatative flag earlier: {} and now made to False", BatchUTIL.flag);
		BatchUTIL.setFlag(false);
	}
    
}
