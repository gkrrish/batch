package com.batch.services.items;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.batch.model.SimpleCacheObject;
import com.batch.notification.email.EmailModel;
import com.batch.notification.email.EmailNotificationService;
import com.batch.repository.SimpleCacheObjectRepository;

@Service
public class ProcessService {
	
	@Autowired
    private SimpleCacheObjectRepository scoRepository;
	@Autowired
    private EmailNotificationService emailNotificationService;
    private EmailModel emailModel;


	public List<SimpleCacheObject> process(List<SimpleCacheObject> scoList) throws Exception {
		scoList.sort(Comparator.comparing(SimpleCacheObject::getNewsPaperfileName));
		
		String currentFileName = "";
		byte[] pdfByteNewsPaper = null;
		
		List<SimpleCacheObject> batchList = new ArrayList<>();
		List<SimpleCacheObject> unProcessedList=new ArrayList<>();
		
	    for (SimpleCacheObject sco : scoList) {
	        String fileName = sco.getNewsPaperfileName();
	        
	        if (!fileName.equals(currentFileName)) {
	            pdfByteNewsPaper = fetchFile(fileName);
	            currentFileName = fileName;
	        }
	        
	        
	        //actually email was not there, appending here for it
	        sco.setEmail("gkrrish.11@gmail.com");
	        String sendEmail = sendEmail(pdfByteNewsPaper, sco.getEmail());
	        if(sendEmail.equals("Email-sent with attachment successfully")) {
	        	batchList.add(sco);
	        }else {
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
	
	
	private String sendEmail(byte[] pdfByteNewsPaper, String email) throws Exception {
		 emailModel = new EmailModel();
		 emailModel.setToEmailId(email);
		 emailModel.setEmailSubject("Notification : e-paper " + LocalDate.now());
		 emailModel.setFromEmailId("notification@newsonwhatsapp.com");
		 emailModel.setEmailBody("\nDear Subscriber,\nPlease find the newspaper attachment.\n\nThanks,\nNOW-Services India.");
		 emailModel.setActualFile(pdfByteNewsPaper);
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


	private byte[] fetchFile(String fileName) {
		byte[] fileBytes;
		Path filePath = Paths.get(fileName);
		try {
			fileBytes = Files.readAllBytes(filePath);
		} catch (IOException e) {
			System.err.println("Error fetching file: " + fileName);
			throw new RuntimeException("While fetching the newspaper File, we got an issue, unable to process!");
		}
		return fileBytes;
	}

}
