package com.gcp.registration.service;

import com.gcp.registration.domain.User;
import com.gcp.registration.domain.UserDetail;

/**
 * @author Anuj Kumar
 * 
 * This interface is service layer to declare DB operations
 */
public interface UserService {
	
	/**
	 * This method is used to write business logic to register user
	 * 
	 * @param user
	 * @return User
	 */
	User registerUser(User user);
	
	/**
	 * This method is used to authenticate the user logon
	 * 
	 * @param userDetail
	 * @return String
	 */
	String findUserByLogonIdAndPassword(UserDetail userDetail);
}
