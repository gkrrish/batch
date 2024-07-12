package com.batch.items;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import com.batch.exception.RedisCacheNotClearedException;
import com.batch.service.NewspaperService;
import com.batch.util.BatchUTIL;

public class NewspaperItemWriter implements ItemWriter<String> {
	NewspaperService newspaperService;

	public NewspaperItemWriter(NewspaperService newspaperService) {
		this.newspaperService = newspaperService;
	}

	@Override
	public void write(Chunk<? extends String> chunk) throws Exception {
		String currentRedisKey = null;
		try {
			currentRedisKey = chunk.getItems().get(0).toString();
			newspaperService.clearKeyOnWriter(currentRedisKey);
			BatchUTIL.flag=true;
		} catch (Exception e) {
			throw new RedisCacheNotClearedException("Not cleared the Redis Cache for Key : " + currentRedisKey);
		}
	}

}
