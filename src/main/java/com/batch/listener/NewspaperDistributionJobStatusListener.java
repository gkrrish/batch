package com.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class NewspaperDistributionJobStatusListener implements JobExecutionListener {

	private static final Logger log = LoggerFactory.getLogger(NewspaperDistributionJobStatusListener.class);

	@Override
	public void beforeJob(JobExecution jobExecution) {
		log.info(":: Job started at :  " + jobExecution.getStartTime());
		log.info("JobExecution more details : " + jobExecution.toString());
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		log.info(":: JobExecution end time at : " + jobExecution.getEndTime());
	}
}