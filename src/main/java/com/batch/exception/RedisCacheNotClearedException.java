package com.batch.exception;

public class RedisCacheNotClearedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public RedisCacheNotClearedException(String message) {
		super(message);
	}

}
