package com.batch.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.batch.service.util.MasterBatchJobRetrivingCacheService;

@Service
public class NewspaperService {

	private final int BEFORE_TIME_IN_MINUTES = 15;

	@Autowired
	private MasterBatchJobRetrivingCacheService masterBatchJobRetrivingCacheService;
	
	@Autowired
	private RedisCacheService redisCacheService;

	public String getCurrentTimeBatchId() {
		Optional<Long> currentBatchId = masterBatchJobRetrivingCacheService.findCurrentBatchId(BEFORE_TIME_IN_MINUTES);
		return currentBatchId.isPresent() ? currentBatchId.get().toString() : "Sorry! Our systems were unable to find any batches scheduled near the delivery time.";
	}
	
	public void clearKeyOnWriter(String currentRedisKey) {
		redisCacheService.evictBatchCache(currentRedisKey);
		
	}
	
	
}
