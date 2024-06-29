package com.batch.items;

import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import com.batch.external.CacheUpdateService;
import com.batch.model.RedisCacheObject;
import com.batch.service.CacheService;
import com.batch.service.NewspaperDistributionService;
import com.batch.transformer.SimpleCacheObject;

public class NewspaperItemReader implements ItemReader<List<String>> {

	@Autowired
	private CacheUpdateService cacheUpdateService;
	@Autowired
	private CacheService cacheService;

	NewspaperDistributionService newspaperService;

	private boolean batchProcessed = false;

	public NewspaperItemReader(NewspaperDistributionService newspaperService) {
		this.newspaperService = newspaperService;
	}

	@Override
	public List<String> read() throws Exception {
		String updateCacheByBatchId=null;

		if (!batchProcessed) {
			batchProcessed = true;
			String currentTimeBatchId = newspaperService.getCurrentTimeBatchId();
			
//			System.out.println("********From Reader************\n : " + currentTimeBatchId + " \n ************************* "+updateCacheByBatchId);

			try {
//				updateCacheByBatchId = cacheUpdateService.updateCacheByBatchId(Long.parseLong(currentTimeBatchId));
				
//				long batchId = Long.parseLong(updateCacheByBatchId.replaceAll("[^\\d]", ""));
				
//				List<SimpleCacheObject> transformedObjectsByBatchId = 
						RedisCacheObject transformedObjectsByBatchId = cacheService.getTransformedObjectsByBatchId(42L);
						
						System.out.println("$$$$$$$$$$$$ "+transformedObjectsByBatchId);
				
//				System.out.println("List <SimpleCachObject> "+transformedObjectsByBatchId);
				
			} catch (NumberFormatException e) {
				System.out.println("********************\n : " + currentTimeBatchId + " \n ************************* "+updateCacheByBatchId);
			}

			return List.of(currentTimeBatchId);
		}
		return null;
	}

}
