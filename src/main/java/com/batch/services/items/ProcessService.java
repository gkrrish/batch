package com.batch.services.items;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.batch.model.SimpleCacheObject;
import com.batch.notification.email.EmailModel;
import com.batch.notification.email.EmailNotificationService;
import com.batch.repository.SimpleCacheObjectRepository;
import com.batch.service.NewspaperService;

@Service
public class ProcessService {

    @Autowired
    NewspaperService newspaperService;

    @Autowired
    private EmailNotificationService emailNotificationService;

    @Autowired
    private SimpleCacheObjectRepository userDeliveryStatus;

    private final ExecutorService executorService = Executors.newFixedThreadPool(10); // Adjust the pool size as needed

    public List<String> process(List<SimpleCacheObject> simpleCacheObjectList) throws Exception {
        // Group by newsPaperfileName
        Map<String, List<SimpleCacheObject>> groupedByFileName = simpleCacheObjectList.stream()
                .collect(Collectors.groupingBy(SimpleCacheObject::getNewsPaperfileName));

        for (Map.Entry<String, List<SimpleCacheObject>> entry : groupedByFileName.entrySet()) {
            String fileName = entry.getKey();
            List<SimpleCacheObject> groupedObjects = entry.getValue();

            // Fetch and cache the original file
            File pdfFile = getOriginalFile(fileName);

            // Send emails to users with the PDF file in batches
            List<List<SimpleCacheObject>> batches = createBatches(groupedObjects, 100); // Batch size of 100

            for (List<SimpleCacheObject> batch : batches) {
                List<CompletableFuture<String>> emailFutures = batch.stream()
                        .map(object -> sendEmailToUser(object, pdfFile))
                        .collect(Collectors.toList());

                CompletableFuture.allOf(emailFutures.toArray(new CompletableFuture[0])).join();
                
                // Log the successfully emailed users to the database
//                userDeliveryStatus.save(null);
            }

            // Delete the cached file
            deleteCachedFile(pdfFile);

            // Remove the key from Redis (assuming a method removeRedisKey is implemented)
            removeRedisKey(fileName);
        }

        return null;
    }

    private File getOriginalFile(String fileName) throws IOException {
        // Simulate fetching the file, replace with actual fetching logic
        File file = new File("/path/to/cache/" + fileName);
        if (!file.exists()) {
            Files.copy(new File("/path/to/original/files/" + fileName).toPath(), file.toPath());
        }
        return file;
    }

    private CompletableFuture<String> sendEmailToUser(SimpleCacheObject object, File pdfFile) {
        EmailModel emailModel = new EmailModel();
        emailModel.setToEmailId(object.getEmail());
        emailModel.setEmailSubject("Your Newspaper PDF");
        emailModel.setEmailBody("Please find the attached PDF file.");
        emailModel.setFileAddress(pdfFile.getAbsolutePath());
        emailModel.setFromEmailId("your-email@example.com");

        return CompletableFuture.supplyAsync(() -> {
            try {
                return emailNotificationService.sendMessageWithAttachment(emailModel).get();
            } catch (Exception e) {
                e.printStackTrace();
                return "Failed to send email";
            }
        }, executorService);
    }

    private void deleteCachedFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }

    private void removeRedisKey(String fileName) {
        // Implement the logic to remove the Redis key
    }

    private List<List<SimpleCacheObject>> createBatches(List<SimpleCacheObject> sourceList, int batchSize) {
        int totalSize = sourceList.size();
        int fullBatches = totalSize / batchSize;
        int remainingItems = totalSize % batchSize;

        List<List<SimpleCacheObject>> batches = new ArrayList<>(fullBatches + (remainingItems > 0 ? 1 : 0));

        for (int i = 0; i < fullBatches; i++) {
            batches.add(sourceList.subList(i * batchSize, (i + 1) * batchSize));
        }

        if (remainingItems > 0) {
            batches.add(sourceList.subList(fullBatches * batchSize, totalSize));
        }

        return batches;
    }
}
