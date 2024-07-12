package com.batch.exception;

public class CurrentBatchIdNotAvailableException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CurrentBatchIdNotAvailableException(String message) {
		super(message);
	}

}
