package com.batch.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonSerialize
@JsonDeserialize
public class RedisCacheObject {

    private Long batchTime;
    private String state;
    private String language;
    private List<UserInfo> users;
    private List<NewspaperInfo> newspapers;

    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class UserInfo {
        private Long userId;
        private String userMobileNumber;
        private String email;
    }

    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class NewspaperInfo {
        private Long newspaperId;
        private Map<String, List<Long>> fileLocations;
    }
}
