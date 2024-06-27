package com.batch.items;

import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import com.batch.service.NewspaperDistributionService;

public class NewspaperItemReader implements ItemReader<List<String>> {

	@Autowired
	NewspaperDistributionService newspaperService;

	private boolean batchProcessed = false;

	@Override
	public List<String> read() throws Exception {

		if (!batchProcessed) {
			batchProcessed = true;
			String currentTimeBatchId = newspaperService.getCurrentTimeBatchId();
			System.out.println("********************\n : " + currentTimeBatchId);
			return List.of(currentTimeBatchId);
		}
		return null;
	}

}
