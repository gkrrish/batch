package com.batch.operation;

import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import com.batch.service.NewspaperDistributionService;

public class NewspaperDistributionItemWriter implements ItemWriter<List<String>> {
	NewspaperDistributionService newspaperService;

	public NewspaperDistributionItemWriter(NewspaperDistributionService newspaperService) {
		this.newspaperService = newspaperService;
	}

	@Override
	public void write(Chunk<? extends List<String>> chunk) throws Exception {

	}

}
