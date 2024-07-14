package com.batch.notification.email;

import java.io.File;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
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
        helper.setText(emailModel.getEmailBody(),true);

        FileSystemResource file = new FileSystemResource(new File(emailModel.getFileAddress()));
        helper.addAttachment(file.getFilename(), file);
        
        mailSender.send(message);

        return CompletableFuture.completedFuture("Email-sent with attachment successfully");
    }
	
	
	/**
	 * Intentionally created this method and doNot want to disturb the existing above method
	 */
	
	@Async
    public CompletableFuture<String> sendMessageWithAttachmentWithByteData(EmailModelWithByteData emailModel) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(emailModel.getFromEmailId());
        helper.setTo(emailModel.getToEmailIds());
        helper.setSubject(emailModel.getEmailSubject());
        helper.setText(emailModel.getEmailBody());//not HTML

        // Attach the PDF byte array
        ByteArrayResource byteArrayResource = new ByteArrayResource(emailModel.getAttachment());
        helper.addAttachment(emailModel.getAttachmentFilename(), byteArrayResource);

        mailSender.send(message);

        return CompletableFuture.completedFuture("Email sent with attachment successfully");
    }

}
