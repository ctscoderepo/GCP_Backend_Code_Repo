package com.gcp.registration.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.gcp.registration.domain.User;

/**
 * @author Anuj Kumar
 * 
 * This is user repository used to perform DB operations
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long>{
	
	/**
	 * This method is used to authenticate the user login in DB
	 * 
	 * @param userDetail
	 * @return User
	 */
	@Query("from User u where u.logonId = :logonId and u.password = :password")
	public User findUserByLogonIdAndPassword(@Param("logonId") String logonId, @Param("password") String password); 
}
