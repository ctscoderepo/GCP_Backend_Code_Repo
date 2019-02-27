package com.gcp.registration.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;


/**
 * @author Anuj Kumar
 * 
 *  This class is to have user logon details
 */
@Data
public class UserDetail {

	@NotBlank(message = "LogonId is required")
	private String logonId;

	@NotBlank(message = "Password is required")
	@Size(min = 8, max = 50, message = "Please use 8 to 50 characters")
	private String password;
}  
