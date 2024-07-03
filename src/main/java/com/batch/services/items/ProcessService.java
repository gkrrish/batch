package com.batch.services.items;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    private SimpleCacheObjectRepository scoRepository;
	@Autowired
    private EmailNotificationService emailNotificationService;
	@Autowired
    private ResourceLoader resourceLoader;
    private EmailModel emailModel;


	public List<SimpleCacheObject> process(List<SimpleCacheObject> scoList) throws Exception {
		scoList.sort(Comparator.comparing(SimpleCacheObject::getNewsPaperfileName));
		
		String currentFileName = "";
		
		List<SimpleCacheObject> batchList = new ArrayList<>();
		List<SimpleCacheObject> unProcessedList=new ArrayList<>();
		
	    for (SimpleCacheObject sco : scoList) {
	        String fileName = sco.getNewsPaperfileName();
	        
	        if (!fileName.equals(currentFileName)) {
	        	savePdfToResources(fileName);
	            currentFileName = fileName;
	        }
	        
	        
	        //actually email was not there, appending here for it
	        sco.setEmail("gkrrish.11@gmail.com");
	        String sendEmail = sendEmail(null, sco.getEmail());
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
	
	
	public void savePdfToResources(String sourceFilePath) throws IOException {
        String fileName = getFileNameFromPath(sourceFilePath);
        
        if (!fileName.endsWith(".pdf")) {
            throw new IllegalArgumentException("The file name must end with .pdf");
        }
        
        Path sourcePath = Paths.get(sourceFilePath.replace("file:///", ""));
        File destinationFolder = new File("src/main/resources/currentFile");
        
        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs();
        }
        
        Path destinationPath = Paths.get(destinationFolder.getAbsolutePath());
        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
    }
    
    private String getFileNameFromPath(String filePath) {
        return filePath.substring(filePath.lastIndexOf('/') + 1);
    }


	private String sendEmail(String fileName, String email) throws Exception {
		 emailModel = new EmailModel();
		 emailModel.setToEmailId(email);
		 emailModel.setEmailSubject("Notification : e-paper " + LocalDate.now());
		 emailModel.setFromEmailId("notification@newsonwhatsapp.com");
		 emailModel.setEmailBody("\nDear Subscriber,\nPlease find the newspaper attachment.\n\nThanks,\nNOW-Services India.");
		 
		Resource resource = resourceLoader.getResource("classpath:files/Eenadu.pdf");
		File srcFile = resource.getFile();
		String srcPath = srcFile.getAbsolutePath();
		
		emailModel.setFileAddress(srcPath);
		 
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
