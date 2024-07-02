package com.batch.notification.email;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.EnableAsync;

import jakarta.mail.MessagingException;

@SpringBootTest
@EnableAsync
@Disabled
public class EmailNotificationsTests {

	@Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private EmailNotificationService emailNotificationService;

    private EmailModel emailModel;

    @BeforeEach
    public void setUp() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:files/Eenadu.pdf");
        File srcFile = resource.getFile();
        String srcPath = srcFile.getAbsolutePath();

        emailModel = new EmailModel();
        emailModel.setFromEmailId("gkrrish.11@gmail.com");
        emailModel.setToEmailId("gkrrish.11@gmail.com");
        emailModel.setEmailSubject("Notification : e-paper " + LocalDate.now());
        emailModel.setEmailBody("***\n TEST CASE \n ****\nDear Subscriber,\nPlease find the newspaper attachment.\n\nThanks,\nNOW-Services India.");
        emailModel.setFileAddress(srcPath);
    }

    @Test
    public void testSendMessageWithAttachment() throws MessagingException, IOException, InterruptedException, ExecutionException {
        CompletableFuture<String> emailSentFuture = emailNotificationService.sendMessageWithAttachment(emailModel);
        String emailSent = emailSentFuture.get(); 
        assertEquals("Email-sent with attachment successfully", emailSent);
    }
}
