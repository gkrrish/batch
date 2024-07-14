package com.batch.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.batch.service.MonthlyInvoiceService;

@Component
public class MonthlyInvoiceSchedular {

	private static final Logger logger = LoggerFactory.getLogger(MonthlyInvoiceSchedular.class);

	@Autowired
	private MonthlyInvoiceService monthlyInvoiceService;

	@Scheduled(cron = "0 0 15 1 * *") // runs every month 1st and at 3pm
	public void preventReaderRepetative() {
		try {
			String sendInvoiceReportByPagination = monthlyInvoiceService.sendInvoiceReportByPagination();
			logger.info(sendInvoiceReportByPagination);
		} catch (Exception e) {
			logger.error("Got failed Invoice Schedular: " + e.getMessage());
		}
	}
}
