package com.batch.listner;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.poc.notifications.EmailNotificationService;

import jakarta.mail.MessagingException;

@Component
public class BatchJobCompletionListener implements JobExecutionListener {

	@Autowired
	private EmailNotificationService emailNotificationService;

	@Override
	public void beforeJob(JobExecution jobExecution) {
		// Logic before job starts (if needed)
		System.out.println("Job is about to start...");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		try {
			emailNotificationService.sendMessageWithAttachment(null);
		} catch (MessagingException e) {
		}
		System.out.println("Job has finished...");
	}
}
