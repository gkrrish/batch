package com.batch.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.batch.model.RedisCacheObject;
import com.batch.transformer.SimpleCacheObject;

@Service
public class CacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public List<SimpleCacheObject> getTransformedObjectsByBatchId(Long batchId) {
        List<SimpleCacheObject> simpleCacheObjects = new ArrayList<>();

        Set<String> keys = redisTemplate.keys("batch:" + batchId + ":state:*:language:*");
        if (keys != null) {
            for (String key : keys) {
                RedisCacheObject cacheObject = (RedisCacheObject) redisTemplate.opsForValue().get(key);
                if (cacheObject != null) {
                    for (RedisCacheObject.UserInfo user : cacheObject.getUsers()) {
                        for (RedisCacheObject.NewspaperInfo newspaper : cacheObject.getNewspapers()) {
                            for (String fileType : newspaper.getFileLocations().keySet()) {
                                for (Long fileId : newspaper.getFileLocations().get(fileType)) {
                                    SimpleCacheObject simpleCacheObject = new SimpleCacheObject();
                                    simpleCacheObject.setBatchTime(cacheObject.getBatchTime());
                                    simpleCacheObject.setState(cacheObject.getState());
                                    simpleCacheObject.setLanguage(cacheObject.getLanguage());
                                    simpleCacheObject.setUserId(user.getUserId());
                                    simpleCacheObject.setUserMobileNumber(user.getUserMobileNumber());
                                    simpleCacheObject.setEmail(user.getEmail());
                                    simpleCacheObject.setNewspaperId(newspaper.getNewspaperId());
                                    simpleCacheObject.setFileLocationType(fileType);
                                    simpleCacheObject.setFileLocationId(fileId);
                                    simpleCacheObjects.add(simpleCacheObject);
                                }
                            }
                        }
                    }
                }
            }
        }
        return simpleCacheObjects;
    }
}
