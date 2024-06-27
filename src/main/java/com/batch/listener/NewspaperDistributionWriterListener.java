package com.batch.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;

public class NewspaperDistributionWriterListener implements ItemWriteListener<List<String>> {
	
	private static final Logger log = LoggerFactory.getLogger(NewspaperDistributionWriterListener.class);
	
	@Override
	public void beforeWrite(Chunk<? extends List<String>> items) {
		log.info("Writer Listner :: before");
	}

	@Override
	public void afterWrite(Chunk<? extends List<String>> items) {
		log.info("Writer Listner :: before");
	}

	@Override
	public void onWriteError(Exception exception, Chunk<? extends List<String>> items) {
		log.info("Writer Listner :: before");
	}

}
