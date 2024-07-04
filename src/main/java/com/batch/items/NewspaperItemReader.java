package com.batch.items;

import java.util.List;

import org.springframework.batch.item.ItemReader;

import com.batch.model.SimpleCacheObject;
import com.batch.services.items.ReaderService;

public class NewspaperItemReader implements ItemReader<List<SimpleCacheObject>> {

	ReaderService readerService;
	private boolean readComplete = false;

	public NewspaperItemReader(ReaderService readerService) {
		this.readerService = readerService;
	}

	@Override
	public List<SimpleCacheObject> read() throws Exception {
		if (!readComplete) {
			List<SimpleCacheObject> data = readerService.read();
			readComplete = (data == null || data.isEmpty());
			return data;
		} else {
			reset();
			return null; 
		}
	}

	public void reset() {
		this.readComplete = false; 
	}

}
