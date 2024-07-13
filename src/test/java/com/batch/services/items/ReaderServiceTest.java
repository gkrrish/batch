package com.batch.services.items;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.batch.exception.CurrentBatchIdNotAvailableException;
import com.batch.exception.ExternalCacheException;
import com.batch.external.ExternalCacheUpdateService;
import com.batch.model.SimpleCacheObject;
import com.batch.service.NewspaperService;
import com.batch.service.RedisCacheService;
import com.batch.util.BatchUTIL;

@ExtendWith(MockitoExtension.class)
public class ReaderServiceTest {

    @Mock
    private ExternalCacheUpdateService externalCacheUpdateService;
    @Mock
    private RedisCacheService redisCacheService;
    @Mock
    private NewspaperService newspaperService;

    @InjectMocks
    @Spy
    private ReaderService readerService;

    private AtomicBoolean originalFlag;

    @BeforeEach
    public void setUp() {
        originalFlag = (AtomicBoolean) ReflectionTestUtils.getField(BatchUTIL.class, "flag");
    }

    @Test
    public void testRead_FlagSetToTrue() {
        setBatchUTILFlag(true);
        assertNull(readerService.read());
        resetBatchUTILFlag();
    }

    @Test
    @Disabled
    public void testRead_FlagSetToFalse() {
        setBatchUTILFlag(false);
        doReturn(Collections.emptyList()).when(readerService).readOperation();
        assertNull(readerService.read());
        resetBatchUTILFlag();
    }

    @Test
    public void testReadOperation_SuccessfulDataRetrieval() {
        String currentTimeBatchId = "123";
        Long batchId = 123L;
        List<SimpleCacheObject> expectedList = List.of(new SimpleCacheObject());

        when(newspaperService.getCurrentTimeBatchId()).thenReturn(currentTimeBatchId);
        when(redisCacheService.isNotPresentCacheInRedis(batchId)).thenReturn(false);
        when(redisCacheService.getRedisCachedObject(batchId)).thenReturn(expectedList);

        List<SimpleCacheObject> actualList = readerService.readOperation();
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testReadOperation_ExceptionThrown() {
        when(newspaperService.getCurrentTimeBatchId()).thenThrow(new RuntimeException("Exception"));

        assertNull(readerService.readOperation());
    }

    @Test
    public void testGetCurrentBatchId_Success() {
        String expectedBatchId = "123";
        when(newspaperService.getCurrentTimeBatchId()).thenReturn(expectedBatchId);
        assertEquals(expectedBatchId, readerService.getCurrentBatchId());
    }

    @Test
    public void testGetCurrentBatchId_Failure() {
        String failedBatchId = "Failed to retrieve";
        when(newspaperService.getCurrentTimeBatchId()).thenReturn(failedBatchId);
        assertThrows(CurrentBatchIdNotAvailableException.class, () -> readerService.getCurrentBatchId());
    }

    @Test
    public void testGetUpdatedRedisCacheKey_CachePresent() {
        Long batchId = 123L;
        when(redisCacheService.isNotPresentCacheInRedis(batchId)).thenReturn(false);
        assertEquals(batchId, readerService.getUpdatedRedisCacheKey(batchId));
    }

    @Test
    public void testGetUpdatedRedisCacheKey_CacheNotPresent_Success() {
        Long batchId = 123L;
        String externalServiceResponse = "125";
        when(redisCacheService.isNotPresentCacheInRedis(batchId)).thenReturn(true);
        when(externalCacheUpdateService.updateCacheByBatchId(batchId)).thenReturn(externalServiceResponse);
        assertEquals(125L, readerService.getUpdatedRedisCacheKey(batchId));
    }

    @Test
    public void testGetUpdatedRedisCacheKey_CacheNotPresent_Failure() {
        Long batchId = 123L;
        String failedResponse = "Failed to update";
        when(redisCacheService.isNotPresentCacheInRedis(batchId)).thenReturn(true);
        when(externalCacheUpdateService.updateCacheByBatchId(batchId)).thenReturn(failedResponse);
        assertThrows(ExternalCacheException.class, () -> readerService.getUpdatedRedisCacheKey(batchId));
    }

    private void setBatchUTILFlag(boolean value) {
        AtomicBoolean flag = new AtomicBoolean(value);
        ReflectionTestUtils.setField(BatchUTIL.class, "flag", flag);
    }

    private void resetBatchUTILFlag() {
        ReflectionTestUtils.setField(BatchUTIL.class, "flag", originalFlag);
    }
}
