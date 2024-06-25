package com.batch.listener;

import java.util.List;

import org.springframework.batch.core.SkipListener;

public class NewspaperDistributionSkipListener implements SkipListener<List<String>, List<String>> {

	@Override
	public void onSkipInRead(Throwable t) {
	}

	@Override
	public void onSkipInWrite(List<String> item, Throwable t) {
	}

	@Override
	public void onSkipInProcess(List<String> item, Throwable t) {
	}

}
