package com.gcp.registration.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author Anuj Kumar
 * 
 *  This class is to have user logon details
 */
public class UserDetail {

	@NotBlank(message = "LogonId is required")
	private String logonId;

	@NotBlank(message = "Password is required")
	@Size(min = 8, max = 12, message = "Please use 8 to 12 characters")
	private String password;

	public String getLogonId() {
		return logonId;
	}

	public void setLogonId(String logonId) {
		this.logonId = logonId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "logonId: " + logonId + "password: "+password;
	}
}
