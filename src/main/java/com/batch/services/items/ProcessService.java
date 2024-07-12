package com.batch.services.items;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.batch.exception.UnProcessedEmailException;
import com.batch.model.SimpleCacheObject;
import com.batch.notification.email.EmailModel;
import com.batch.notification.email.EmailNotificationService;
import com.batch.repository.SimpleCacheObjectRepository;
import com.batch.util.BatchUTIL;

import jakarta.validation.constraints.NotEmpty;
import lombok.NonNull;

@Service
public class ProcessService {

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private SimpleCacheObjectRepository scoRepository;
	@Autowired
	private EmailNotificationService emailNotificationService;

	public String process(@NonNull @NotEmpty List<SimpleCacheObject> scoList) throws Exception {
		scoList.sort(Comparator.comparing(SimpleCacheObject::getNewsPaperfileName));

		List<SimpleCacheObject> batchList = new ArrayList<>();
		String currentFileName = "";
		String fileSourcePath = null;
		List<String> emailIdsListForaFile = new ArrayList<>();

		for (SimpleCacheObject sco : scoList) {
			String fileName = sco.getNewsPaperfileName();

			if (!fileName.equals(currentFileName)) {
				Resource resource = resourceLoader.getResource("file:" + fileName);
				fileSourcePath = getFilePathName(resource);
				currentFileName = fileName;
			}
			if (fileSourcePath != null) {
				emailIdsListForaFile.add(sco.getEmail());
				batchList.add(sco);
			}
			if (emailIdsListForaFile.size() >= 100) {
				sendEmailBatch(fileSourcePath, emailIdsListForaFile, batchList);
				emailIdsListForaFile.clear();
				batchList.clear();
			}

		}
		if (!emailIdsListForaFile.isEmpty()) {//validate this scenario later if we may get more than expected size with all different files combined?
			sendEmailBatch(fileSourcePath, emailIdsListForaFile, batchList);
			emailIdsListForaFile.clear();
			batchList.clear();
		}

		return scoList.get(0).getCurrentRedisKey();
	}

	private String getFilePathName(Resource resource) throws IOException {
		return resource.exists() ? resource.getFile().getAbsolutePath() : null;
	}


	@Transactional
	public void batchSave(List<SimpleCacheObject> simpleCacheObjectList) {
		int batchSize = 100;
		for (int i = 0; i < simpleCacheObjectList.size(); i += batchSize) {
			int end = Math.min(i + batchSize, simpleCacheObjectList.size());
			List<SimpleCacheObject> batchList = simpleCacheObjectList.subList(i, end);
			scoRepository.saveAll(batchList);
			scoRepository.flush();
		}
	}

	private void sendEmailBatch(String fileSourcePath, List<String> emailIdsListForaFile, List<SimpleCacheObject> batchList) throws Exception {
		try {
			String isSentEmail = sendEmail(fileSourcePath, emailIdsListForaFile);

			if (isSentEmail.equals("Email-sent with attachment successfully")) {
				batchList.forEach(sco -> sco.setIsProcessed("Y"));
			}
		} catch (Exception e) {
			batchList.forEach(sco -> sco.setIsProcessed("N"));
		}
		batchSave(batchList);
	}
	
	private String sendEmail(String fileName, List<String> emailIds) throws Exception {
		EmailModel emailModel = new EmailModel();
		//emailModel.setToEmailIds(emailIds.toArray(new String[0]));
		//testing---
		emailModel.setToEmailIds(new String[] {"kallemkishan204@gmail.com"});
		//--testing
		emailModel.setEmailSubject("Notification : e-paper " + LocalDate.now());
		emailModel.setFromEmailId(BatchUTIL.FROM_EMAIL_ID);
		emailModel.setEmailBody(BatchUTIL.getMailBody());
		emailModel.setFileAddress(fileName);
		CompletableFuture<String> emailSentFuture = emailNotificationService.sendMessageWithAttachment(emailModel);
		return emailSentFuture.get();
	}
	
	/**
	 * for this method create another scheduler
	 * @Scheduled(cron = "0 0 * * * ?") // Adjust the cron expression as needed
        public void resendFailedEmails() {
        processService.resendFailedEmails();
       }
	 */
	 public void resendFailedEmails() {
	        List<SimpleCacheObject> failedEmails = null;//scoRepository.findByIsProcessed("N");

	        List<SimpleCacheObject> emailsToResend = failedEmails.stream()
	        													  .filter(email -> !email.getIsProcessed().equals("Y"))
	        													  .collect(Collectors.toList());

	        if (!emailsToResend.isEmpty()) {
	            try {
	                process(emailsToResend);
	            } catch (Exception e) {
	                throw new UnProcessedEmailException("Unable to send the emails to 'N' status on the email status table tried second time ");
	            }
	        }
	    }
}
