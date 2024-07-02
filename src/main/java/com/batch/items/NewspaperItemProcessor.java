package com.batch.items;

import java.util.List;

import org.springframework.batch.item.ItemProcessor;

import com.batch.model.SimpleCacheObject;
import com.batch.services.items.ProcessService;

public class NewspaperItemProcessor implements ItemProcessor<List<SimpleCacheObject>, List<String>> {
	
	ProcessService processService;

	public NewspaperItemProcessor(ProcessService processService) {
		this.processService = processService;
	}

	@Override
	public List<String> process(List<SimpleCacheObject> simpleCacheObjectList) throws Exception {
		return processService.process(simpleCacheObjectList);
	}//this class made for simplicity

}
