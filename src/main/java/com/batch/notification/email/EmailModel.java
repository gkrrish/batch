package com.batch.notification.email;

import lombok.Data;

@Data
public class EmailModel {
	
	private String[] toEmailIds;
	private String emailSubject;
	private String emailBody;
	private String fileAddress;
	private String fromEmailId;
}
