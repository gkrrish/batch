package com.batch.services.items;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.batch.exception.CurrentBatchIdNotAvailableException;
import com.batch.exception.ExternalCacheException;
import com.batch.external.ExternalCacheUpdateService;
import com.batch.items.NewspaperItemReader;
import com.batch.model.SimpleCacheObject;
import com.batch.service.NewspaperService;
import com.batch.service.RedisCacheService;
import com.batch.util.BatchUTIL;

@Service
public class ReaderService {

	private static final Logger logger = LoggerFactory.getLogger(NewspaperItemReader.class);

	@Autowired
	private ExternalCacheUpdateService externalCacheUpdateService;
	@Autowired
	private RedisCacheService redisCacheService;
	@Autowired
	private NewspaperService newspaperService;

	/**
	 * This method mainly meant for reading the data from the Redis and keeps in simple Cache Object
	 * 
	 * @return
	 */
	public List<SimpleCacheObject> read() {
		return BatchUTIL.getFlag() ? null : readOperation();
	}

	/**
	 * This method will do a complete Read operations, so separated a method for more readable and maintainable.
	 * Here catch-ed the thrown exceptions entire read operations
	 * @return
	 */
	private List<SimpleCacheObject> readOperation() {
		try {
			
			String currentTimeBatchId = getCurrentBatchId();
			Long batchId = Long.parseLong(currentTimeBatchId);
			batchId = getUpdatedRedisCacheKey(batchId);
			List<SimpleCacheObject> simpleCacheObjectList = redisCacheService.getRedisCachedObject(batchId);
			return simpleCacheObjectList.isEmpty() ? null : simpleCacheObjectList;
			
		}catch (Exception e) {
			logger.error("Read Operation got terminated Due to the given reason "+e.getMessage());
			return null;
		}
	}

	/**
	 * 
	 * @return get the current time's batch Id
	 */
	public String getCurrentBatchId() {
		String currentTimeBatchId = newspaperService.getCurrentTimeBatchId();

		if (currentTimeBatchId.isEmpty() || currentTimeBatchId.startsWith("Failed")) {
			logger.error("Error retrieving current time batch ID: {}", currentTimeBatchId);
			throw new CurrentBatchIdNotAvailableException(currentTimeBatchId);
		}
		return currentTimeBatchId;
	}

	/**
	 * 
	 * @param batchId
	 * @return get the Current Batch of the Redis cache, if cache is not there means we are going to create the cache
	 */
	public Long getUpdatedRedisCacheKey(Long batchId) {
		if (redisCacheService.isNotPresentCacheInRedis(batchId)) {
			String externalRedisServiceResponse = externalCacheUpdateService.updateCacheByBatchId(batchId);

			if (externalRedisServiceResponse.isEmpty() || externalRedisServiceResponse.startsWith("Failed")) {
				logger.error("Error retrieving external cache from Redis ID: {}", externalRedisServiceResponse);
				throw new ExternalCacheException(externalRedisServiceResponse);
			}
			batchId = Long.parseLong(externalRedisServiceResponse.replaceAll("[^\\d]", ""));
		}
		return batchId;
	}

}
