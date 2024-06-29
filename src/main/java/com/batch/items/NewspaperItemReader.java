package com.batch.items;

import java.util.List;

import org.springframework.batch.item.ItemReader;

import com.batch.services.items.ReaderService;

public class NewspaperItemReader implements ItemReader<List<String>> {

	ReaderService readerService;

	public NewspaperItemReader(ReaderService readerService) {
		this.readerService = readerService;
	}

	@Override
	public List<String> read() throws Exception {
		return readerService.read();
	}
	//this class made very simplicity

}
