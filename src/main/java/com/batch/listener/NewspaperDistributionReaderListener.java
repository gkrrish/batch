package com.batch.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;

public class NewspaperDistributionReaderListener implements ItemReadListener<List<String>> {
	
	private static final Logger log = LoggerFactory.getLogger(NewspaperDistributionReaderListener.class);

	@Override
	public void beforeRead() {
		log.info("Reader Listner :: before");
	}

	@Override
	public void afterRead(List<String> item) {
		log.info("Reader Listner :: before");
	}

	@Override
	public void onReadError(Exception ex) {
		log.info("Reader Listner :: before");
	}

}
