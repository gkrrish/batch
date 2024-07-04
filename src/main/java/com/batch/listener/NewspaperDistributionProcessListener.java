package com.batch.listener;

import java.util.List;

import org.springframework.batch.core.ItemProcessListener;

public class NewspaperDistributionProcessListener implements ItemProcessListener<List<String>, List<String>> {
	

	@Override
	public void beforeProcess(List<String> item) {
		System.out.println("Process :: Before");
	}

	@Override
	public void afterProcess(List<String> item, List<String> result) {
		System.out.println("Process :: After");
	}

	@Override
	public void onProcessError(List<String> item, Exception e) {
	}

}
