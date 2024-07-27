package com.batch.external;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class ExternalCacheUpdateServiceTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ExternalCacheUpdateService externalCacheUpdateService;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testUpdateCache_Positive() {
        String expectedResponse = "Cache updated successfully";
        mockServer.expect(requestTo("http://localhost:9002/cache/update"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(expectedResponse, org.springframework.http.MediaType.TEXT_PLAIN));

        String response = externalCacheUpdateService.updateCache();

        assertEquals(expectedResponse, response);
        mockServer.verify();
    }

    @Test
    void testUpdateCache_Negative() {
        mockServer.expect(requestTo("http://localhost:9002/cache/update"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(RuntimeException.class, () -> {
            externalCacheUpdateService.updateCache();
        });
        mockServer.verify();
    }

    @Test
    void testUpdateCacheByBatchId_Positive() {
        Long batchId = 123L;
        String expectedResponse = "Cache updated successfully for batch " + batchId;
        mockServer.expect(requestTo("http://localhost:9002/cache/update/batch/" + batchId))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(expectedResponse, org.springframework.http.MediaType.TEXT_PLAIN));

        String response = externalCacheUpdateService.updateCacheByBatchId(batchId);

        assertEquals(expectedResponse, response);
        mockServer.verify();
    }

    @Test
    void testUpdateCacheByBatchId_Negative() {
        Long batchId = 123L;
        mockServer.expect(requestTo("http://localhost:9002/cache/update/batch/" + batchId))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(RuntimeException.class, () -> {
            externalCacheUpdateService.updateCacheByBatchId(batchId);
        });
        mockServer.verify();
    }

    @Test
    void testUpdateCacheByBatchIdAndState_Positive() {
        Long batchId = 123L;
        String stateName = "state1";
        String expectedResponse = "Cache updated successfully for batch " + batchId + " and state " + stateName;
        mockServer.expect(requestTo("http://localhost:9002/cache/update/batch/" + batchId + "/state/" + stateName))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(expectedResponse, org.springframework.http.MediaType.TEXT_PLAIN));

        String response = externalCacheUpdateService.updateCacheByBatchIdAndState(batchId, stateName);

        assertEquals(expectedResponse, response);
        mockServer.verify();
    }

    @Test
    void testUpdateCacheByBatchIdAndState_Negative() {
        Long batchId = 123L;
        String stateName = "state1";
        mockServer.expect(requestTo("http://localhost:9002/cache/update/batch/" + batchId + "/state/" + stateName))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(RuntimeException.class, () -> {
            externalCacheUpdateService.updateCacheByBatchIdAndState(batchId, stateName);
        });
        mockServer.verify();
    }

    @Test
    void testUpdateCacheByBatchIdStateAndLanguage_Positive() {
        Long batchId = 123L;
        String stateName = "state1";
        String language = "en";
        String expectedResponse = "Cache updated successfully for batch " + batchId + ", state " + stateName + ", and language " + language;
        mockServer.expect(requestTo("http://localhost:9002/cache/update/batch/" + batchId + "/state/" + stateName + "/language/" + language))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(expectedResponse, org.springframework.http.MediaType.TEXT_PLAIN));

        String response = externalCacheUpdateService.updateCacheByBatchIdStateAndLanguage(batchId, stateName, language);

        assertEquals(expectedResponse, response);
        mockServer.verify();
    }

    @Test
    void testUpdateCacheByBatchIdStateAndLanguage_Negative() {
        Long batchId = 123L;
        String stateName = "state1";
        String language = "en";
        mockServer.expect(requestTo("http://localhost:9002/cache/update/batch/" + batchId + "/state/" + stateName + "/language/" + language))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(RuntimeException.class, () -> {
            externalCacheUpdateService.updateCacheByBatchIdStateAndLanguage(batchId, stateName, language);
        });
        mockServer.verify();
    }
}
