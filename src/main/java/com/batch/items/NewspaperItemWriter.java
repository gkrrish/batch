package com.batch.items;

import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import com.batch.service.NewspaperService;

public class NewspaperItemWriter implements ItemWriter<List<String>> {
	NewspaperService newspaperService;

	public NewspaperItemWriter(NewspaperService newspaperService) {
		this.newspaperService = newspaperService;
	}

	@Override
	public void write(Chunk<? extends List<String>> chunk) throws Exception {
		 chunk.getItems().forEach(System.out::println);
	}

}
