package com.batch.listener;

import java.util.List;

import org.springframework.batch.core.SkipListener;

public class NewspaperSkipListener implements SkipListener<List<String>, List<String>> {

	@Override
    public void onSkipInRead(Throwable t) {
        System.err.println("Skipped item during read due to: " + t.getMessage());
    }

    @Override
    public void onSkipInWrite(List<String> item, Throwable t) {
        System.err.println("Skipped item during write: " + item + " due to: " + t.getMessage());
    }

    @Override
    public void onSkipInProcess(List<String> item, Throwable t) {
        System.err.println("Skipped item during process: " + item + " due to: " + t.getMessage());
    }

}
