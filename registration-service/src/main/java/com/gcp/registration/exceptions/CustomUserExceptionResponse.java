package com.gcp.registration.exceptions;

/**
 * @author Anuj Kumar
 * 
 * To hold the logonId
 */
public class CustomUserExceptionResponse {
	
	private String logonId;
	
	public CustomUserExceptionResponse(String logonId) {
		super();
		this.logonId = logonId;
	}

	public String getLogonId() {
		return logonId;
	}

	public void setLogonId(String logonId) {
		this.logonId = logonId;
	}
}
