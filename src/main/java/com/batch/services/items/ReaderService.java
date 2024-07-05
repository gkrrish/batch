package com.batch.services.items;

import java.time.LocalTime;
import java.util.ArrayList;
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

	public List<SimpleCacheObject> read() throws Exception {
		
		System.out.println("From :: ReaderService :"+LocalTime.now());
		
		List<SimpleCacheObject> redisScoList = new ArrayList<SimpleCacheObject>();
		String externalRedisServiceResponse = null;

		String currentTimeBatchId = newspaperService.getCurrentTimeBatchId();
		
		System.out.println("From :: ReaderService : Current BatchId- "+currentTimeBatchId);

		try {
			System.out.println("Inside a TRY ::"+Long.parseLong(currentTimeBatchId));
			externalRedisServiceResponse = externalCacheUpdateService.updateCacheByBatchId(Long.parseLong(currentTimeBatchId));// Returns String, parse to Long
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
