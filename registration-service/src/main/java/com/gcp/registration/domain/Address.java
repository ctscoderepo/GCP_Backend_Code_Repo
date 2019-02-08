package com.gcp.registration.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


/**
 * @author Anuj Kumar
 * 
 * This class is domain to create address table
 */
@Entity
public class Address {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter @Setter private Long addressId;
    
	@NotBlank(message="First name is required")
    @Size(max = 60)
	@Getter @Setter private String firstName;
	
	@NotBlank(message="Last name is required")
	@Size(max = 60)
	@Getter @Setter private String lastName;
		
	@NotBlank(message="Email is required")	
	@Size(max = 120)
	@Getter @Setter private String email;
	
    @Size(max = 15)
    @Getter @Setter private String phoneNumber;

    @Temporal(TemporalType.DATE)
    @Getter @Setter private Date dateOfBirth;

    @Size(max = 100)
    @Getter @Setter private String address1;

    @Size(max = 100)
    @Getter @Setter private String address2;

    @Size(max = 100)
    @Getter @Setter private String city;

    @Size(max = 100)
    @Getter @Setter private String state;

    @Size(max = 100)
    @Getter @Setter private String country;

    @Size(max = 32)
    @Getter @Setter private String zipCode;
    
    @Size(max = 2)
    @Getter @Setter private String status;
    
    @Size(max = 3)
    @Getter @Setter private String addressType;
    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User user;

    public Address() {

    }

    public Address(String firstName,String lastName, String email, String phoneNumber, Date dateOfBirth,
                       String address1, String address2, String city, 
                       String state, String country, String zipCode, String status, String addressType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    	this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipCode = zipCode;
        this.status = zipCode;
        this.addressType = addressType;
    }

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}   	
}
