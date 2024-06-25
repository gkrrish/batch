package com.batch.writer;

import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class CustomItemWriter implements ItemWriter<String> {
    public void write(List<? extends String> items) {
    }

	@Override
	public void write(Chunk<? extends String> chunk) throws Exception {
		
	}
}
