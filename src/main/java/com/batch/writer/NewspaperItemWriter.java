package com.batch.writer;

import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import com.batch.service.NewspaperService;

public class NewspaperItemWriter implements ItemWriter<List<String>> {

	public NewspaperItemWriter(NewspaperService newspaperService) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void write(Chunk<? extends List<String>> chunk) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
