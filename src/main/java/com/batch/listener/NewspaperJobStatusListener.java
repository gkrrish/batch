package com.batch.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class NewspaperJobStatusListener implements JobExecutionListener {


	@Override
	public void beforeJob(JobExecution jobExecution) {
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
	}
}