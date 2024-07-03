package com.batch.items;

import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import com.batch.model.SimpleCacheObject;
import com.batch.service.NewspaperService;

public class NewspaperItemWriter implements ItemWriter<List<SimpleCacheObject>> {
	NewspaperService newspaperService;

	public NewspaperItemWriter(NewspaperService newspaperService) {
		this.newspaperService = newspaperService;
	}

	@Override
	public void write(Chunk<? extends List<SimpleCacheObject>> chunk) throws Exception {
		String currentRedisKey = chunk.getItems().stream().findFirst().get().stream().findFirst().get().getCurrentRedisKey();
		newspaperService.clearKeyOnWriter(currentRedisKey);
		
		
		
	}

}
