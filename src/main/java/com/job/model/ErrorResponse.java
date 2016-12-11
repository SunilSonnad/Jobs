package com.job.model;

/**
 * Customer error message with status and message.
 * 
 */
public class ErrorResponse {
	
	public int status;
	public String message;
	
	public ErrorResponse(int code, String message) {
		this.status = code;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
