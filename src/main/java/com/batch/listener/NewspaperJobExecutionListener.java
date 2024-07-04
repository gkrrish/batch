package com.batch.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import com.batch.items.NewspaperItemReader;

/**
 * This listener manages the state of the NewspaperItemReader before and after
 * the execution of a job. It ensures that the reader's state is reset before
 * each job execution to avoid any leftover state from previous executions,
 * which can cause issues such as infinite loops or incorrect processing.
 */
public class NewspaperJobExecutionListener implements JobExecutionListener {

	private NewspaperItemReader newspaperItemReader;

	public NewspaperJobExecutionListener(NewspaperItemReader newspaperItemReader) {
		this.newspaperItemReader = newspaperItemReader;
	}

	/**
	 * This method is called before the job starts. It resets the state of the
	 * ItemReader to ensure that it starts fresh for the new job execution.
	 *
	 * @param jobExecution the JobExecution context
	 */
	@Override
	public void beforeJob(JobExecution jobExecution) {
		newspaperItemReader.reset();
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
	}

}
