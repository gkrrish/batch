package com.batch.items;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import com.batch.service.NewspaperService;

public class NewspaperItemWriter implements ItemWriter<String> {
	NewspaperService newspaperService;

	public NewspaperItemWriter(NewspaperService newspaperService) {
		this.newspaperService = newspaperService;
	}

	@Override
	public void write(Chunk<? extends String> chunk) throws Exception {
		System.out.println("\n\n From WRITER service");
        String currentRedisKey = chunk.getItems().stream()
                                       .findFirst()
                                       .orElseThrow(() -> new IllegalStateException("Chunk is empty"));
                                       
        System.out.println("From Writer Service :currentRedisKey::  " + currentRedisKey);
        newspaperService.clearKeyOnWriter(currentRedisKey);
	}


}
