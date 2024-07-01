package com.batch.listener;

import java.util.List;

import org.springframework.batch.core.ItemProcessListener;

public class NewspaperDistributionProcessListener implements ItemProcessListener<List<String>, List<String>> {
	

	@Override
	public void beforeProcess(List<String> item) {
	}

	@Override
	public void afterProcess(List<String> item, List<String> result) {
	}

	@Override
	public void onProcessError(List<String> item, Exception e) {
	}

}
