package com.batch.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.batch.model.RedisCacheObject;
import com.batch.model.SimpleCacheObject;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RedisCacheService {

	@Autowired
	private StringRedisTemplate redisTemplate;

	public List<SimpleCacheObject> getRedisCachedObject(Long batchId) {
		 List<SimpleCacheObject> simpleCacheObjects = null ;

		Set<String> keys = redisTemplate.keys("batch:" + batchId + ":state:*:language:*");
		
		if (keys != null) {
			for (String key : keys) {
				simpleCacheObjects = SimpleCacheObject.transform(getDataFromCache(key), key);
			}
		}

		return simpleCacheObjects;

	}

	public RedisCacheObject getDataFromCache(String key) {
		String data = redisTemplate.opsForValue().get(key);
		if (data != null) {
			try {
				return new ObjectMapper().readValue(data, RedisCacheObject.class);

			} catch (Exception e) {
				e.getMessage();
			}
		}else if(data==null || data.isEmpty()) {
			System.out.println(" Cache is Empty or external data is Empty : key not found on Redis "+key);
			throw new RuntimeException(" Cache is Empty or external data is Empty : key not found on Redis "+key);
		}
		return null;
	}
	
	public void evictBatchCache(String currentkey) {
        Set<String> keys = redisTemplate.keys(currentkey);
        if (keys != null) {
            redisTemplate.delete(keys);
        }
    }
}
