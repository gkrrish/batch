package com.batch.items;

import java.util.List;

import org.springframework.batch.item.ItemProcessor;

public class NewspaperItemProcessor implements ItemProcessor<List<String>, List<String>> {

	@Override
	public List<String> process(List<String> item) throws Exception {
		return null;
	}

}
