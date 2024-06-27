package com.batch.items;

import java.util.List;

import org.springframework.batch.item.ItemReader;

import com.batch.service.NewspaperDistributionService;

public class NewspaperItemReader implements ItemReader<List<String>> {

	NewspaperDistributionService newspaperService;
	
	private boolean batchProcessed = false;

	public NewspaperItemReader(NewspaperDistributionService newspaperService) {
		this.newspaperService = newspaperService;
	}

	@Override
	public List<String> read() throws Exception {

		if (!batchProcessed) {
			batchProcessed = true;
			String currentTimeBatchId = newspaperService.getCurrentTimeBatchId();
			
			System.out.println("********From Reader************\n : " + currentTimeBatchId+" \n *************************");
			
			return List.of(currentTimeBatchId);
		}
		return null;
	}

}
