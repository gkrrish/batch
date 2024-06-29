package com.batch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.batch.model.RedisCacheObject;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RedisCacheService {

	@Autowired
	private StringRedisTemplate redisTemplate;

	public RedisCacheObject getRedisCachedObject(Long batchId) {

		String key = "batch:" + batchId + ":state:*:language:*";
		return getDataFromCache(key);

	}

	public RedisCacheObject getDataFromCache(String key) {
		String data = redisTemplate.opsForValue().get(key);
		if (data != null) {
			try {
				return new ObjectMapper().readValue(data, RedisCacheObject.class);
				
			} catch (Exception e) { e.getMessage(); }
		}
		return null;
	}
}
