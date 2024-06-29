package com.batch.transformer;

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
    private String fileLocationType;
    private Long fileLocationId;
}