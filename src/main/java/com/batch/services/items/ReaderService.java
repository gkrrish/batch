package com.batch.services.items;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.batch.external.ExternalCacheUpdateService;
import com.batch.model.SimpleCacheObject;
import com.batch.service.RedisCacheService;

@Service
public class ReaderService {

	@Autowired
	private ExternalCacheUpdateService externalCacheUpdateService;
	@Autowired
	private RedisCacheService redisCacheService;
	

	public List<SimpleCacheObject> read(Long currentTimeBatchId) throws Exception {
		
		List<SimpleCacheObject> redisScoList = null;
		String externalRedisServiceResponse = null;

		try {
			
			if(redisCacheService.isCachePresent(currentTimeBatchId)) {
				externalRedisServiceResponse = externalCacheUpdateService.updateCacheByBatchId(currentTimeBatchId);// Returns String, parse to Long
			}
			
			if(externalRedisServiceResponse.equals("Failed to update cache for batchId:")) {
				System.out.println("Debug : there is not Active users nor Today newspapers present.");
				return null;
			}
			long batchId = Long.parseLong(externalRedisServiceResponse.replaceAll("[^\\d]", ""));// OK--gives the batchId
			
			System.out.println("From :: ReaderService : batchId Rediss Before : "+batchId);

			redisScoList = redisCacheService.getRedisCachedObject(batchId);
			System.out.println("From :: ReaderService :"+ redisScoList.toString());

			if (redisScoList.isEmpty()) {
				return null;
			}

		} catch (NumberFormatException e) {
			System.out.println("Number Format Exception :: "+currentTimeBatchId);
			return null;
		} catch (ResourceAccessException resourceAccessException) {
			System.out.println(resourceAccessException.getMessage());
			return null;
		} catch (Exception e) {
			throw new RuntimeException("Have the issue : " + e.getMessage());
		}

		return redisScoList;
	}

}
