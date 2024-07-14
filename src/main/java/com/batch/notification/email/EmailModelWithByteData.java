package com.batch.notification.email;

import lombok.Data;

@Data
public class EmailModelWithByteData {
	
	private String[] toEmailIds;
    private String emailSubject;
    private String emailBody;
    private String fromEmailId;

    private byte[] attachment;
    private String attachmentFilename;
    /*
     * This calls is created intentionally to do NOT disturb the existing model
     */
}