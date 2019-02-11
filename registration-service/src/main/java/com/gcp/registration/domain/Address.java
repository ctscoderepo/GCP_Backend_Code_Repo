package com.gcp.registration.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Anuj Kumar
 * 
 * This class is domain to create address table
 */
@Data
@Entity
@Table(name = "address")
public class Address {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    
	@NotBlank(message="First name is required")
	private String firstName;
	
	@NotBlank(message="Last name is required")
	private String lastName;
		
	@NotBlank(message="Email is required")	
	private String email;
	
    private String phoneNumber;

    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String country;
    private String zipCode;      
    private String status;    
    private String addressType;
    
    @OneToOne(mappedBy = "address")
    private User user;
}
