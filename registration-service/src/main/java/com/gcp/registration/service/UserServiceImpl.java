package com.gcp.registration.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.gcp.registration.domain.User;
import com.gcp.registration.domain.UserDetail;
import com.gcp.registration.exceptions.LogonIdException;
import com.gcp.registration.repository.UserRepository;

/**
 * @author Anuj Kumar
 * 
 *         This class is service layer having implementation of UserService
 *         interface
 */
@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	protected UserRepository userRepository;

	/*@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;*/

	/**
	 * This method is having implementation of registerUser method to interact
	 * with DB using user repository
	 * 
	 * @param user
	 * @return User
	 */
	@Override
	public User registerUser(User user) {
		logger.info("Start registerUser method:", UserServiceImpl.class.getName());
		logger.debug("User request: ", user.toString());
		try {
			// To set the logonId in upper case
			user.setLogonId(user.getLogonId().toUpperCase());

			// To encript the password
			//user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

			return userRepository.save(user);
		} catch (Exception ex) {
			logger.error("Exception: ", ex.getMessage());
			throw new LogonIdException("User with logonId " + user.getLogonId() + " already exists.");
		}
	}

	/**
	 * This method is find user using logonId and password
	 * 
	 * @param userDetail
	 * @return String
	 */
	public String findUserByLogonIdAndPassword(UserDetail userDetail) {
		logger.info("Start findUserByLogonIdAndPassword method:", UserServiceImpl.class.getName());
		logger.debug("Logon details :", userDetail);
		String response = "";

		System.out.println("*************: "+userDetail.getLogonId()+" "+userDetail.getPassword());
		
		User user = userRepository.findUserByLogonIdAndPassword(userDetail.getLogonId(), userDetail.getPassword());
	
		if(user!=null)
			response = "User login successful.";
		else
			response = "Invalid logonId/password.";		
		return response;
	}
}
