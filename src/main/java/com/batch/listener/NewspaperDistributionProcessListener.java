package com.batch.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;

public class NewspaperDistributionProcessListener implements ItemProcessListener<List<String>, List<String>> {
	
	private static final Logger log = LoggerFactory.getLogger(NewspaperDistributionProcessListener.class);

	@Override
	public void beforeProcess(List<String> item) {
		log.info("Process Listner :: before");
	}

	@Override
	public void afterProcess(List<String> item, List<String> result) {
		log.info("Process Listner :: after processing");
	}

	@Override
	public void onProcessError(List<String> item, Exception e) {
		log.info("Process Listner :: onProcessing");
	}

}
