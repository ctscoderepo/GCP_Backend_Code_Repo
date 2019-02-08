package com.gcp.registration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Anuj Kumar
 * 
 * Class to define custom runtime exception to while checking logonId existence.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LogonIdException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public LogonIdException(String message) {
		super(message);
	}
}
