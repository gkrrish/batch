package com.batch.listener;

import java.util.List;

import org.springframework.batch.core.ItemReadListener;

public class NewspaperDistributionReaderListener implements ItemReadListener<List<String>> {

	@Override
	public void beforeRead() {
	}

	@Override
	public void afterRead(List<String> item) {
	}

	@Override
	public void onReadError(Exception ex) {
	}

}
