package com.batch.external;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalCacheUpdateService {

    @Autowired
    private RestTemplate restTemplate;

    private final String baseUrl = "http://localhost:9002/cache";

    public String updateCache() {
        String url = baseUrl + "/update";
        return restTemplate.getForObject(url, String.class);
    }

    public String updateCacheByBatchId(Long batchId) {
        String url = baseUrl + "/update/batch/" + batchId;
        return restTemplate.getForObject(url, String.class);
    }

    public String updateCacheByBatchIdAndState(Long batchId, String stateName) {
        String url = baseUrl + "/update/batch/" + batchId + "/state/" + stateName;
        return restTemplate.getForObject(url, String.class);
    }

    public String updateCacheByBatchIdStateAndLanguage(Long batchId, String stateName, String language) {
        String url = baseUrl + "/update/batch/" + batchId + "/state/" + stateName + "/language/" + language;
        return restTemplate.getForObject(url, String.class);
    }
}