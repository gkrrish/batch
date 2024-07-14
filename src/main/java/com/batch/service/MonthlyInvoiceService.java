package com.batch.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.batch.model.UserDetails;
import com.batch.model.WelcomeRequest;
import com.batch.notification.email.EmailModelWithByteData;
import com.batch.notification.email.EmailNotificationService;
import com.batch.repository.UserDetailsRepository;

import jakarta.mail.MessagingException;

@Service
public class MonthlyInvoiceService {

    private static final Logger logger = LoggerFactory.getLogger(MonthlyInvoiceService.class);

    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private EmailNotificationService emailNotificationService;
    @Autowired
    private RestTemplate restTemplate;

    public String sendInvoiceReportByPagination() throws IOException, MessagingException {

        int batchSize = 100;
        long totalRecords = userDetailsRepository.count();
        int totalPages = (int) Math.ceil((double) totalRecords / batchSize);

        for (int pageNumber = 0; pageNumber < totalPages; pageNumber++) {
            int startRow = pageNumber * batchSize + 1;
            int endRow = (pageNumber + 1) * batchSize;
            
            List<UserDetails> userDetailsList = userDetailsRepository.findAllWithStartAndEndRows(startRow, endRow);
            
            Set<UserDetails> userDetailsSet = userDetailsList.stream().collect(Collectors.toSet());
            userDetailsList = userDetailsSet.stream().collect(Collectors.toList());
            
            sendInvoiceCopy(userDetailsList);

            logger.info("Invoice schedular sends reports from row: {} to row: {}", startRow, endRow);
        }
        
        return "Successfully Sent Invoice PDF file to Subscribers";
    }

    public void sendInvoiceCopy(List<UserDetails> userDetailsList) throws IOException, MessagingException {

        for (UserDetails userDetails : userDetailsList) {
            WelcomeRequest welcomeRequest = new WelcomeRequest();
            welcomeRequest.setWelcomeAnyMessage("Called from Batch services to send Invoice Monthly Report");
            welcomeRequest.setMobileNumber(userDetails.getMobileNumber());
            welcomeUser(welcomeRequest);
        }
    }

    public void welcomeUser(WelcomeRequest request) throws IOException, MessagingException {

        String url = "http://localhost:8383/welcome";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<WelcomeRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.POST, entity, byte[].class);

        if (response.getStatusCode() == HttpStatus.OK) {
            byte[] pdfBytes = response.getBody();
            String attachmentFilename = request.getMobileNumber() + ".pdf";
            sendEmailWithAttachment(pdfBytes, attachmentFilename);
        } else {
            logger.info("Failed to generate invoice: {}", response.getStatusCode());
        }
    }

    private void sendEmailWithAttachment(byte[] pdfBytes, String attachmentFilename) throws MessagingException {
    	String previousMonthYear = LocalDate.now().minusMonths(1).getMonth().toString() + "-" + LocalDate.now().minusMonths(1).getYear();

    	EmailModelWithByteData emailModel = new EmailModelWithByteData();
        emailModel.setFromEmailId("kallemkishan204@gmail.com"); 
        emailModel.setToEmailIds(new String[]{"kallemkishan204@gmail.com"});
        emailModel.setEmailSubject("Invoice : "+previousMonthYear);
        
        String emailBody = "Dear Subscriber,\n\n" +
                "Thank you for being with us. Please find the attached invoice report.\n\n" +
                "Thanks,\n" +
                "Batch Operations\n" +
                "Invoice Department\n" +
                "Now-Services India"+
                "\n\n\n"
                +"మీ ఆనందాలతో వేడుక చేసుకుందాం, మీ స్నేహితులకు మా గురించి చెప్పండి. షేర్ చేయండి. "; //this should be done through localization
        
        emailModel.setEmailBody(emailBody);
        emailModel.setAttachment(pdfBytes);
        emailModel.setAttachmentFilename(attachmentFilename);

        emailNotificationService.sendMessageWithAttachmentWithByteData(emailModel);
    }
}
