package com.batch.listener;

import java.util.List;

import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;

public class NewspaperDistributionWriterListener implements ItemWriteListener<List<String>> {

	@Override
	public void beforeWrite(Chunk<? extends List<String>> items) {
	}

	@Override
	public void afterWrite(Chunk<? extends List<String>> items) {
	}

	@Override
	public void onWriteError(Exception exception, Chunk<? extends List<String>> items) {
	}

}
