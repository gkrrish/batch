package com.batch.notification.email;

import java.io.File;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailNotificationService {

	@Autowired
	private JavaMailSender mailSender;

	@Async
    public CompletableFuture<String> sendMessageWithAttachment(EmailModel emailModel) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(emailModel.getFromEmailId());
        helper.setTo(emailModel.getToEmailIds());
        helper.setSubject(emailModel.getEmailSubject());
        helper.setText(emailModel.getEmailBody());

        FileSystemResource file = new FileSystemResource(new File(emailModel.getFileAddress()));
        helper.addAttachment(file.getFilename(), file);
        
        System.out.println("From Email : "+emailModel.toString());
        
        mailSender.send(message);

        return CompletableFuture.completedFuture("Email-sent with attachment successfully");
    }

}
