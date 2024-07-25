package com.batch.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.batch.model.RedisCacheObject;
import com.batch.model.SimpleCacheObject;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.embedded.RedisServer;

@ExtendWith(MockitoExtension.class)
class RedisCacheServiceTest {

    private static RedisServer redisServer;

    @Mock
    private StringRedisTemplate redisTemplate;

    @InjectMocks
    private RedisCacheService redisCacheService;

    @BeforeAll
    static void setUp() throws IOException {
        redisServer = new RedisServer(6379);
        redisServer.start();
    }

    @AfterAll
    static void tearDown() {
        redisServer.stop();
    }

   /* @Test
    void testGetRedisCachedObject_Positive() throws Exception {
        Long batchId = 123L;
        String key = "batch:123:state:1:language:en";
        Set<String> keys = new HashSet<>(Arrays.asList(key));

        RedisCacheObject cacheObject = new RedisCacheObject("some data");

        ValueOperations<String, String> valueOps = mock(ValueOperations.class);
        when(redisTemplate.keys("batch:" + batchId + ":state:*:language:*")).thenReturn(keys);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get(key)).thenReturn(new ObjectMapper().writeValueAsString(cacheObject));

        List<SimpleCacheObject> result = redisCacheService.getRedisCachedObject(batchId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("some data", result.get(0).getData());
    }*/

    @Test
    void testGetRedisCachedObject_Negative() {
        Long batchId = 123L;

        when(redisTemplate.keys("batch:" + batchId + ":state:*:language:*")).thenReturn(null);

        List<SimpleCacheObject> result = redisCacheService.getRedisCachedObject(batchId);

        assertNull(result);
    }

    @Test
    void testIsNotPresentCacheInRedis_Positive() {
        Long batchId = 123L;

        when(redisTemplate.keys("batch:" + batchId + ":state:*:language:*")).thenReturn(new HashSet<>());

        boolean result = redisCacheService.isNotPresentCacheInRedis(batchId);

        assertTrue(result);
    }

    @Test
    void testIsNotPresentCacheInRedis_Negative() {
        Long batchId = 123L;
        Set<String> keys = new HashSet<>(Arrays.asList("key1", "key2"));

        when(redisTemplate.keys("batch:" + batchId + ":state:*:language:*")).thenReturn(keys);

        boolean result = redisCacheService.isNotPresentCacheInRedis(batchId);

        assertFalse(result);
    }

    @Test
    void testGetDataFromCache_Positive() throws Exception {
        String key = "batch:123:state:1:language:en";
        RedisCacheObject cacheObject = new RedisCacheObject("some data");

        @SuppressWarnings("unchecked")
		ValueOperations<String, String> valueOps = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get(key)).thenReturn(new ObjectMapper().writeValueAsString(cacheObject));

        RedisCacheObject result = redisCacheService.getDataFromCache(key);

        assertNotNull(result);
        assertEquals("some data", result.getData());
    }

    @Test
    void testGetDataFromCache_Negative() {
        String key = "batch:123:state:1:language:en";

        @SuppressWarnings("unchecked")
		ValueOperations<String, String> valueOps = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get(key)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            redisCacheService.getDataFromCache(key);
        });
        assertEquals("Cache is empty or external data is empty: key not found in Redis " + key, exception.getMessage());
    }

    @Test
    void testEvictBatchCache_Positive() {
        String key = "batch:123:state:*:language:*";
        Set<String> keys = new HashSet<>(Arrays.asList("key1", "key2"));

        when(redisTemplate.keys(key)).thenReturn(keys);

        redisCacheService.evictBatchCache(key);

        verify(redisTemplate).delete(keys);
    }

    @Test
    void testEvictBatchCache_Negative() {
        String key = "batch:123:state:*:language:*";

        when(redisTemplate.keys(key)).thenReturn(null);

        redisCacheService.evictBatchCache(key);

        verify(redisTemplate, never()).delete(anySet());
    }
}
