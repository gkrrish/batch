package com.batch.items;

import java.util.List;

import org.springframework.batch.item.ItemReader;

import com.batch.model.SimpleCacheObject;
import com.batch.services.items.ReaderService;

public class NewspaperItemReader implements ItemReader<List<SimpleCacheObject>> {

	ReaderService readerService;

	public NewspaperItemReader(ReaderService readerService) {
		this.readerService = readerService;
	}

	@Override
	public List<SimpleCacheObject> read() {
		return readerService.read();
	}
}
