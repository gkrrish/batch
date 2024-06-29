package com.batch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.batch.model.RedisCacheObject;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CacheService {
	
	 @Autowired
	    private StringRedisTemplate redisTemplate;

	public RedisCacheObject getTransformedObjectsByBatchId(Long batchId) {

		String key = "batch:42:state:Telangana:language:Telugu";
//		redisTemplate.opsForValue().get(key);
		return getDataFromCache(key);

	}
	
	public RedisCacheObject getDataFromCache(String key) {
        String data = redisTemplate.opsForValue().get(key);
        if (data != null) {
            try {
                return new ObjectMapper().readValue(data, RedisCacheObject.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
