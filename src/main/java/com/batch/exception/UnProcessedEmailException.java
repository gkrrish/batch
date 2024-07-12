package com.batch.exception;

public class UnProcessedEmailException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UnProcessedEmailException(String message) {
		super(message);
	}

}
