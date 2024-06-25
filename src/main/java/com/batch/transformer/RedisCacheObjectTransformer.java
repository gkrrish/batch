package com.batch.transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.batch.model.RedisCacheObject;

public class RedisCacheObjectTransformer {

    public List<SimpleCacheObject> transform(RedisCacheObject redisCacheObject) {
        List<SimpleCacheObject> simpleCacheObjects = new ArrayList<>();

        if (redisCacheObject.getUsers() != null && redisCacheObject.getNewspapers() != null) {
            simpleCacheObjects = redisCacheObject.getUsers().parallelStream()
                    .flatMap(user -> redisCacheObject.getNewspapers().parallelStream()
                            .flatMap(newspaper -> newspaper.getFileLocations().entrySet().parallelStream()
                                    .flatMap(entry -> entry.getValue().parallelStream()
                                            .map(fileId -> {
                                                SimpleCacheObject simpleCacheObject = new SimpleCacheObject();
                                                simpleCacheObject.setBatchTime(redisCacheObject.getBatchTime());
                                                simpleCacheObject.setState(redisCacheObject.getState());
                                                simpleCacheObject.setLanguage(redisCacheObject.getLanguage());
                                                simpleCacheObject.setUserId(user.getUserId());
                                                simpleCacheObject.setUserMobileNumber(user.getUserMobileNumber());
                                                simpleCacheObject.setEmail(user.getEmail());
                                                simpleCacheObject.setNewspaperId(newspaper.getNewspaperId());
                                                simpleCacheObject.setFileLocationType(entry.getKey());
                                                simpleCacheObject.setFileLocationId(fileId);
                                                return simpleCacheObject;
                                            }))))
                    .collect(Collectors.toList());
        }

        return simpleCacheObjects;
    }
}
