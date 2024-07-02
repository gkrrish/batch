package com.batch.model;

import java.util.ArrayList;
import java.util.List;

import com.batch.model.RedisCacheObject.NewspaperInfo;
import com.batch.model.RedisCacheObject.UserInfo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SimpleCacheObject {
    private Long batchTime;
    private String state;
    private String language;
    private Long userId;
    private String userMobileNumber;
    private String email;
    private Long newspaperId;
    private Long newspaperFileId;
    private String newsPaperfileName;
    
    public static List<SimpleCacheObject> transform(RedisCacheObject redisCacheObject) {
        List<SimpleCacheObject> simpleCacheObjects = new ArrayList<>();

        for (UserInfo user : redisCacheObject.getUsers()) {
            for (NewspaperInfo newspaper : redisCacheObject.getNewspapers()) {
                if (newspaper.getAssociateNewspaperIds().contains(user.getNewspaperId())) {
                    SimpleCacheObject simpleCacheObject = new SimpleCacheObject();
                    simpleCacheObject.setBatchTime(redisCacheObject.getBatchTime());
                    simpleCacheObject.setState(redisCacheObject.getState());
                    simpleCacheObject.setLanguage(redisCacheObject.getLanguage());
                    simpleCacheObject.setUserId(user.getUserId());
                    simpleCacheObject.setUserMobileNumber(user.getUserMobileNumber());
                    simpleCacheObject.setEmail(user.getEmail());
                    simpleCacheObject.setNewspaperId(user.getNewspaperId());
                    simpleCacheObject.setNewspaperFileId(newspaper.getNewspaperFileId());
                    simpleCacheObject.setNewsPaperfileName(newspaper.getNewsPaperfileName());

                    simpleCacheObjects.add(simpleCacheObject);
                }
            }
        }
        return simpleCacheObjects;
    }
}