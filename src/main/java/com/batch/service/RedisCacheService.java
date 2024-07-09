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
		
		System.out.println("From RedisCacheService :: keys :"+keys.toString());
		
		if(keys.isEmpty()) {
			return null;
		}
		
		if (keys != null) {
			for (String key : keys) {
				RedisCacheObject dataFromCache = getDataFromCache(key);
				System.out.println("From RedisCacheService :: after "+dataFromCache.toString());
				
				simpleCacheObjects = SimpleCacheObject.transform(dataFromCache, key);
				System.out.println("From RedisCacheService :: after Transform "+simpleCacheObjects.toString());
				
			}
		}

		return simpleCacheObjects;

	}
	/**
	 * 
	 * @param batchId
	 * @return by using this method we are minimizing the calls for external cache service.
	 */
	public boolean isCachePresent(Long batchId) {
		Set<String> keys = redisTemplate.keys("batch:" + batchId + ":state:*:language:*");
		return (keys.isEmpty()|| keys.size()==0) ? true : false;
	}

	public RedisCacheObject getDataFromCache(String key) {
		String data = redisTemplate.opsForValue().get(key);
		
		if (data != null) {
			try {
				System.out.println("Get Data From Cache : "+data.toString() +"for Key : "+key);
				return new ObjectMapper().readValue(data, RedisCacheObject.class);

			} catch (Exception e) {
				System.err.println(" Cache is Empty or external data is Empty : key not found on Redis "+key);
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
        System.out.println("Cache Evicted :: From REDIS CACHE SERVICE, I came through WRITER service ::");
    }
	
	
}
