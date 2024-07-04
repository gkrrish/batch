package com.batch.listener;

import java.util.List;

import org.springframework.batch.core.ItemReadListener;

public class NewspaperDistributionReaderListener implements ItemReadListener<List<String>> {
	

	@Override
	public void beforeRead() {
		System.out.println("Before :: Read");
	}

	@Override
	public void afterRead(List<String> item) {
		System.out.println("Before :: After");
	}

	@Override
	public void onReadError(Exception ex) {
	}

}
