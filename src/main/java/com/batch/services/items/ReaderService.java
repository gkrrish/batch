package com.batch.services.items;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.batch.external.ExternalCacheUpdateService;
import com.batch.model.SimpleCacheObject;
import com.batch.service.NewspaperService;
import com.batch.service.RedisCacheService;

@Service
public class ReaderService {
	
	@Autowired
	private ExternalCacheUpdateService externalCacheUpdateService;
	@Autowired
	private RedisCacheService redisCacheService;
	@Autowired
	NewspaperService newspaperService;

	private boolean batchProcessed = false;
	
	
	public List<String> read() throws Exception {
		String externalRedisServiceResponse=null;

		if (!batchProcessed) {
			batchProcessed = true;
			String currentTimeBatchId = newspaperService.getCurrentTimeBatchId();
			
			try {
				externalRedisServiceResponse = externalCacheUpdateService.updateCacheByBatchId(Long.parseLong(currentTimeBatchId));//Returns String, parse to Long
				long batchId = Long.parseLong(externalRedisServiceResponse.replaceAll("[^\\d]", ""));//OK--gives the batchId

				List<SimpleCacheObject> redisSimpleCachedObject = redisCacheService.getRedisCachedObject(batchId);
				
				System.out.println("********\nRedisCacheObject : "+redisSimpleCachedObject+"\n***********");
				
			} catch (NumberFormatException e) {
				System.out.println(currentTimeBatchId);
				throw new RuntimeException(currentTimeBatchId);
			}catch(ResourceAccessException resourceAccessException) {
				System.out.println(resourceAccessException.getMessage());
			}
			catch (Exception e) {
				throw new RuntimeException("Have the issue : "+e.getMessage());
			}

			return List.of(currentTimeBatchId);
		}
		return null;
	}

}
