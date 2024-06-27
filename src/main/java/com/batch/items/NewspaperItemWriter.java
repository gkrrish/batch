package com.batch.items;

import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import com.batch.service.NewspaperDistributionService;

public class NewspaperItemWriter implements ItemWriter<List<String>> {
	NewspaperDistributionService newspaperService;

	public NewspaperItemWriter(NewspaperDistributionService newspaperService) {
		this.newspaperService = newspaperService;
	}

	@Override
	public void write(Chunk<? extends List<String>> chunk) throws Exception {
		 chunk.getItems().forEach(System.out::println);
	}

}
