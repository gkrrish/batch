package com.batch.services.items;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.batch.external.ExternalCacheUpdateService;
import com.batch.model.RedisCacheObject;
import com.batch.service.NewspaperDistributionService;
import com.batch.service.RedisCacheService;

@Service
public class ReaderService {
	
	@Autowired
	private ExternalCacheUpdateService externalCacheUpdateService;
	@Autowired
	private RedisCacheService redisCacheService;
	@Autowired
	NewspaperDistributionService newspaperService;

	private boolean batchProcessed = false;
	
	
	public List<String> read() throws Exception {
		String externalRedisServiceResponse=null;

		if (!batchProcessed) {
			batchProcessed = true;
			String currentTimeBatchId = newspaperService.getCurrentTimeBatchId();
			
			try {
				externalRedisServiceResponse = externalCacheUpdateService.updateCacheByBatchId(Long.parseLong(currentTimeBatchId));//Returns String, parse to Long
				long batchId = Long.parseLong(externalRedisServiceResponse.replaceAll("[^\\d]", ""));//OK--gives the batchId

				RedisCacheObject redisCachedObject = redisCacheService.getRedisCachedObject(batchId);
				
				System.out.println("********\nRedisCacheObject : "+redisCachedObject+"\n***********");
				
			} catch (NumberFormatException e) {
				throw new RuntimeException("Unable to update the Redis Cache , there is issue with External Redis Serive :"+e.getMessage()+" : "+externalRedisServiceResponse);
			}catch (Exception e) {
				throw new RuntimeException("Have the issue : "+e.getMessage());
			}

			return List.of(currentTimeBatchId);
		}
		return null;
	}

}
