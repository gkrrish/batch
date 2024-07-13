package com.batch.notification.email;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.File;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableAsync;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@ExtendWith(MockitoExtension.class)
@EnableAsync
public class EmailNotificationsTests {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailNotificationService emailNotificationService;

    private EmailModel emailModel;

    @BeforeEach
    public void setUp() {
        emailModel = new EmailModel();
        emailModel.setFromEmailId("gkrrish.11@gmail.com");
        emailModel.setToEmailIds(new String[]{"gkrrish.11@gmail.com"});
        emailModel.setEmailSubject("Notification : e-paper " + LocalDate.now());
        emailModel.setEmailBody("***\n TEST CASE \n ****\nDear Subscriber,\nPlease find the newspaper attachment.\n\nThanks,\nNOW-Services India.");
        emailModel.setFileAddress("src/test/resources/files/Eenadu.pdf");
    }

    @Test
    public void testSendMessageWithAttachment() throws MessagingException, InterruptedException, ExecutionException {
        MimeMessage mimeMessage = mock(MimeMessage.class);

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        CompletableFuture<String> emailSentFuture = emailNotificationService.sendMessageWithAttachment(emailModel);
        String emailSent = emailSentFuture.get();

        assertEquals("Email-sent with attachment successfully", emailSent);

        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessage);
    }
}
