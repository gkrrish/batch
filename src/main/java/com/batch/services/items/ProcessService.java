package com.batch.services.items;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.batch.model.SimpleCacheObject;
import com.batch.notification.email.EmailModel;
import com.batch.notification.email.EmailNotificationService;
import com.batch.repository.SimpleCacheObjectRepository;

@Service
public class ProcessService {
	
	@Autowired
    private ResourceLoader resourceLoader;

	@Autowired
	private SimpleCacheObjectRepository scoRepository;
	@Autowired
	private EmailNotificationService emailNotificationService;
	private EmailModel emailModel;

	public List<SimpleCacheObject> process(List<SimpleCacheObject> scoList) throws Exception {
		scoList.sort(Comparator.comparing(SimpleCacheObject::getNewsPaperfileName));

		List<SimpleCacheObject> batchList = new ArrayList<>();
		List<SimpleCacheObject> unProcessedList = new ArrayList<>();

		String currentFileName = "";
		Resource resource=null;
		String srcPath=null;

		for (SimpleCacheObject sco : scoList) {
			String fileName = sco.getNewsPaperfileName();

			if (!fileName.equals(currentFileName)) {
				resource = resourceLoader.getResource("file:" + fileName);
				if (resource.exists()) {
					File srcFile = resource.getFile();
					srcPath = srcFile.getAbsolutePath();
					currentFileName = fileName;
				} else {
					System.err.println("File not found: " + fileName);
				}
			}

			if (resource != null) {
				sco.setEmail("gkrrish.11@gmail.com");
				String sendEmail = sendEmail(srcPath, sco.getEmail());

				if (sendEmail.equals("Email-sent with attachment successfully")) {
					batchList.add(sco);
				} else {
					unProcessedList.add(sco);
				}
			} else {
				unProcessedList.add(sco);
			}
			if (batchList.size() >= 50) {
				batchSave(batchList);
				batchList.clear();
			}
		}
		if (!batchList.isEmpty()) {
			batchSave(batchList);
		}
		return unProcessedList;
	}

	private String sendEmail(String fileName, String email) throws Exception {
		emailModel = new EmailModel();
		emailModel.setToEmailId(email);
		emailModel.setEmailSubject("Notification : e-paper " + LocalDate.now());
		emailModel.setFromEmailId("gkrrish.11@gmail.com");
		emailModel.setEmailBody(
				"\nDear Subscriber,\nPlease find the newspaper attachment.\n\nThanks,\nBatch Operations\nNOW-Services India.");
		emailModel.setFileAddress(fileName);

		CompletableFuture<String> emailSentFuture = emailNotificationService.sendMessageWithAttachment(emailModel);
		return emailSentFuture.get();
	}

	@Transactional
	public void batchSave(List<SimpleCacheObject> simpleCacheObjectList) {
		int batchSize = 50;
		for (int i = 0; i < simpleCacheObjectList.size(); i += batchSize) {
			int end = Math.min(i + batchSize, simpleCacheObjectList.size());
			List<SimpleCacheObject> batchList = simpleCacheObjectList.subList(i, end);
			scoRepository.saveAll(batchList);
			scoRepository.flush();
		}
	}

}
