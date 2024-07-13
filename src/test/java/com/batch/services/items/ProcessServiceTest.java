package com.batch.services.items;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.batch.model.SimpleCacheObject;
import com.batch.notification.email.EmailNotificationService;
import com.batch.repository.SimpleCacheObjectRepository;

@ExtendWith(MockitoExtension.class)
public class ProcessServiceTest {

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private SimpleCacheObjectRepository scoRepository;

    @Mock
    private EmailNotificationService emailNotificationService;

    @InjectMocks
    private ProcessService processService;

    private static List<SimpleCacheObject> scoList;
    
    private static final String filePath="C:\\Users\\Gaganam Krishna\\Downloads\\test-newspapers\\Eenadu_TS_04-07-2024.pdf";

    @BeforeAll
    public static void setUp() {
        scoList = new ArrayList<>();
        SimpleCacheObject sco = new SimpleCacheObject();
        sco.setBatchTime(7L);
        sco.setEmail("kallemkishan204@gmail.com");
        sco.setNewsPaperfileName(filePath);
        scoList.add(sco);
    }

    @Test
    public void processTest() throws Exception {
    	when(emailNotificationService.sendMessageWithAttachment(any())).thenReturn(CompletableFuture.completedFuture("Email-sent with attachment successfully"));
        
        Resource resourceMock = mock(Resource.class);
        when(resourceMock.exists()).thenReturn(true);
        when(resourceMock.getFile()).thenReturn(new File(filePath));
        when(resourceLoader.getResource(anyString())).thenReturn(resourceMock);

        String process = processService.process(scoList);

        verify(emailNotificationService, times(1)).sendMessageWithAttachment(any());
        verify(scoRepository, times(1)).saveAll(any());
        verify(scoRepository, times(1)).flush();

        assertNull(process);
    }
}
