package com.batch.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.batch.service.util.BatchJobCache;

@Service
public class NewspaperService {
	
	private static final Logger logger = LoggerFactory.getLogger(NewspaperService.class);

	private final int BEFORE_TIME_IN_MINUTES = 3;

	@Autowired
	private BatchJobCache masterBatchJobRetrivingCacheService;
	
	@Autowired
	private RedisCacheService redisCacheService;

	public String getCurrentTimeBatchId() {
		Optional<Long> currentBatchId = masterBatchJobRetrivingCacheService.findCurrentBatchId(BEFORE_TIME_IN_MINUTES);
		logger.info("Current BatchId : {} ", currentBatchId);
		return currentBatchId.isPresent() ? currentBatchId.get().toString() : "Sorry! Our systems were unable to find any batches scheduled near the delivery time.";
	}
	
	public void clearKeyOnWriter(String currentRedisKey) {
		redisCacheService.evictBatchCache(currentRedisKey);
		logger.info("Redis Cache been Evicted for Key {} ", currentRedisKey);
		
	}
	
	
}
