package com.gcp.registration.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Anuj Kumar
 * 
 *         This class is domain to create User table
 */
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	@Setter
	private Long userId;

	@NotBlank(message = "LogonId is required")
	@Column(unique = true)
	@Getter
	@Setter
	private String logonId;

	@NotBlank(message = "Password is required")
	@Size(min = 8, max = 12, message = "Please use 8 to 12 characters")
	@Getter
	@Setter
	private String password;

	@JsonFormat(pattern = "yyyy-mm-dd")
	@Getter
	@Setter
	private Date created_At;

	@JsonFormat(pattern = "yyyy-mm-dd")
	@Getter
	@Setter
	private Date updated_At;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Getter
	@Setter
	@Valid
	private Address address;

	@PrePersist
	protected void onCreate() {
		this.created_At = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updated_At = new Date();
	}

	public User() {

	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

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

	public Date getCreated_At() {
		return created_At;
	}

	public void setCreated_At(Date created_At) {
		this.created_At = created_At;
	}

	public Date getUpdated_At() {
		return updated_At;
	}

	public void setUpdated_At(Date updated_At) {
		this.updated_At = updated_At;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "logonId: " + logonId + " firstName:" + getAddress().getFirstName() + " lastName: "
				+ getAddress().getLastName() + " email: " + getAddress().getEmail() + " phoneNumber: "
				+ getAddress().getPhoneNumber() + " address1: " + getAddress().getAddress1() + " address2: "
				+ getAddress().getAddress2() + " city: " + getAddress().getCity() + " state: " + getAddress().getState()
				+ " country: " + getAddress().getCountry() + " zipCode: " + getAddress().getZipCode() + " addressType: "
				+ getAddress().getAddressType() + " status: " + getAddress().getStatus();
	}
}
