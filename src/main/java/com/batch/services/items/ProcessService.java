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
		
		System.out.println("From ProcessService :: after sorting "+scoList.toString());

		List<SimpleCacheObject> batchList = new ArrayList<>();
		List<SimpleCacheObject> unProcessedList = new ArrayList<>();

		String currentFileName = "";
		Resource resource=null;
		String srcPath=null;

		for (SimpleCacheObject sco : scoList) {
			String fileName = sco.getNewsPaperfileName();
			
			System.out.println("From ProcessService ::  Inside ForLoop : fileName "+fileName);

			if (!fileName.equals(currentFileName)) {
				resource = resourceLoader.getResource("file:" + fileName);
				if (resource.exists()) {
					File srcFile = resource.getFile();
					srcPath = srcFile.getAbsolutePath();
					
					System.out.println("From ProcessService ::  File Absolute Path : Resource "+srcPath);
					currentFileName = fileName;
				} else {
					System.err.println("File not found: " + fileName);
				}
			}

			if (resource != null) {
				//handle later if user receive paper today, then should not send again, need to give the what time you sent today.
				sco.setEmail("kallemkishan204@gmail.com");
				 sendEmail(srcPath, sco.getEmail());
				
				System.out.println("From ProcessService :: sendEmail :");

				/*if (sendEmail.equals("Email-sent with attachment successfully")||sco.getEmail()!=null) {//||sco.getEmail()!=null remove this condition by-passing purpose I added here
					batchList.add(sco);
				} else {
					unProcessedList.add(sco); //later divide this into another method
				}*/
				batchList.add(sco);
				System.out.println("\n\n\n I think receiving mail is in progress I think so.\n\n\n\n");
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
		if(unProcessedList.isEmpty()) {
			SimpleCacheObject sco=new SimpleCacheObject();
			String currentRedisKey = scoList.get(0).getCurrentRedisKey();
			sco.setCurrentRedisKey(currentRedisKey);
			unProcessedList.add(sco);
		}
		
		System.out.println("**End of the Process Service :: unProcessedList : "+unProcessedList.toString());
		
		return unProcessedList;//mostly here unProcessedList passing because the ItemWriter has to receive the RedisKey so that from here passing, check later thoroughly,and validate scenarios
	}

	private String sendEmail(String fileName, String email) throws Exception {
		emailModel = new EmailModel();
		emailModel.setToEmailId(email);
		emailModel.setEmailSubject("Notification : e-paper " + LocalDate.now());
		emailModel.setFromEmailId("kallemkishan204@gmail.com");
		emailModel.setEmailBody(
				"\nDear Subscriber,\nPlease find the newspaper attachment.\n\nThanks,\nBatch Operations\nNOW-Services India.");
		emailModel.setFileAddress(fileName);

		CompletableFuture<String> emailSentFuture = emailNotificationService.sendMessageWithAttachment(emailModel);
		//future is causing the next operations. Fix this issue ASAP.
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
			
			System.out.println("BATCH SAVE : "+simpleCacheObjectList.toString());
		}
	}

}
