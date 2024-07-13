package com.batch.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.batch.service.util.BatchJobCache;

@ExtendWith(MockitoExtension.class)
public class NewspaperServiceTest {

    @Mock
    private BatchJobCache masterBatchJobRetrivingCacheService;

    @Mock
    private RedisCacheService redisCacheService;

    @InjectMocks
    private NewspaperService newspaperService;

    @BeforeEach
    void setUp() {
        // Initialization, if required, can be done here
    }

    @Test
    public void getCurrentTimeBatchId_Found() {
        Optional<Long> batchId = Optional.of(12345L);
        when(masterBatchJobRetrivingCacheService.findCurrentBatchId(anyInt())).thenReturn(batchId);

        String result = newspaperService.getCurrentTimeBatchId();

        assertEquals("12345", result);
        verify(masterBatchJobRetrivingCacheService, times(1)).findCurrentBatchId(anyInt());
    }

    @Test
    public void getCurrentTimeBatchId_NotFound() {
        Optional<Long> batchId = Optional.empty();
        when(masterBatchJobRetrivingCacheService.findCurrentBatchId(anyInt())).thenReturn(batchId);

        String result = newspaperService.getCurrentTimeBatchId();

        assertEquals("Sorry! Our systems were unable to find any batches scheduled near the delivery time.", result);
        verify(masterBatchJobRetrivingCacheService, times(1)).findCurrentBatchId(anyInt());
    }

    @Test
    public void clearKeyOnWriter_Test() {
        String redisKey = "testRedisKey";

        newspaperService.clearKeyOnWriter(redisKey);

        verify(redisCacheService, times(1)).evictBatchCache(redisKey);
    }
}
