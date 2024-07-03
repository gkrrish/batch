package com.batch.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.batch.model.RedisCacheObject.NewspaperInfo;
import com.batch.model.RedisCacheObject.UserInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "USER_DELIVERY_STATUS")
public class SimpleCacheObject {//this table doesn't have any constraints as of now later modify it.

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_delivery_status_seq")
	@SequenceGenerator(name = "user_delivery_status_seq", sequenceName = "user_delivery_status_seq", allocationSize = 1)
	private Long id;

	@Column(name = "batch_time")
	private Long batchTime;

	@Column(name = "state")
	private String state;

	@Column(name = "language")
	private String language;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "user_mobile_number")
	private String userMobileNumber;

	@Column(name = "email")
	private String email;

	@Column(name = "newspaper_id")
	private Long newspaperId;

	@Column(name = "newspaper_file_id")
	private Long newspaperFileId;

	@Column(name = "newspaper_file_name")
	private String newsPaperfileName;

	@Column(name = "creation_date", updatable = false, insertable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	private String currentRedisKey;
    
    public static List<SimpleCacheObject> transform(RedisCacheObject redisCacheObject, String currentRedisKey) {
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
                    
                    simpleCacheObject.setCurrentRedisKey(currentRedisKey);

                    simpleCacheObjects.add(simpleCacheObject);
                }
            }
        }
        return simpleCacheObjects;
    }
}