package com.batch.listener;

import java.util.List;

import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;

public class NewspaperWriterListener implements ItemWriteListener<List<String>> {
	
	
	@Override
	public void beforeWrite(Chunk<? extends List<String>> items) {
		System.out.println("Write :: Before");
	}

	@Override
	public void afterWrite(Chunk<? extends List<String>> items) {
		System.out.println("Write :: After");
	}

	@Override
	public void onWriteError(Exception exception, Chunk<? extends List<String>> items) {
	}

}
