package com.batch.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.batch.model.RedisCacheObject;
import com.batch.model.SimpleCacheObject;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NonNull;

@Service
public class RedisCacheService {

	private static final Logger logger = LoggerFactory.getLogger(RedisCacheService.class);

	@Autowired
	private StringRedisTemplate redisTemplate;

	/**
	 * 
	 * @param batchId
	 * @return returning the List<SimpleCacheObject from Redis-Cache, by giving the batchId, we are fetching the cache.
	 */
	public List<SimpleCacheObject> getRedisCachedObject(@NonNull Long batchId) {//think about while saving the object into Redis, another Data structure? check in cache project
		Set<String> keys = redisTemplate.keys("batch:" + batchId + ":state:*:language:*");

		if (keys == null || keys.isEmpty()) return null;

		List<SimpleCacheObject> simpleCacheObjects = new ArrayList<>();

		for (String key : keys) {
			RedisCacheObject dataFromCache = getDataFromCache(key);
			List<SimpleCacheObject> transformedObjects = SimpleCacheObject.transform(dataFromCache, key);
			if (transformedObjects != null && !transformedObjects.isEmpty()) {
				simpleCacheObjects.addAll(transformedObjects);
			}
		}

		return simpleCacheObjects.isEmpty() ? null : simpleCacheObjects;
	}

	/**
	 * 
	 * @param batchId
	 * @return by using this method we are minimizing the calls for external cache
	 *         service.
	 */
	public boolean isNotPresentCacheInRedis(Long batchId) {
		Set<String> keys = redisTemplate.keys("batch:" + batchId + ":state:*:language:*");
		return keys.isEmpty();
	}

	/**
	 * 
	 * @param key
	 * @return by taking the key, we are going to return the RedisCacheObject from
	 *         the Redis cache.
	 */
	public RedisCacheObject getDataFromCache(String key) {
		String data = redisTemplate.opsForValue().get(key);

		if (data != null) {
			try {
				logger.info("Get Data From Cache for Key: {}", key);
				return new ObjectMapper().readValue(data, RedisCacheObject.class);
			} catch (Exception e) {
				logger.error("Error parsing cache data for key {}: {}", key, e.getMessage());
				throw new RuntimeException("Error parsing cache data for key " + key, e);
			}
		}

		logger.warn("Cache is empty or external data is empty: key not found in Redis {}", key);
		throw new RuntimeException("Cache is empty or external data is empty: key not found in Redis " + key);
	}

	/**
	 * 
	 * @param current key Deleting the Redis Cache, it is mostly doing after
	 *                   completing the read operations.
	 */
	public void evictBatchCache(String currentkey) {
		Set<String> keys = redisTemplate.keys(currentkey);
		if (keys != null) {
			redisTemplate.delete(keys);
		}
	}

}
