package com.gcp.cart.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "orderitems")
public class OrderItems {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private long memberId;
	private long addressId;
	private String productId;
	private double price;
	private double shippingCharge;
	private double tax;
	private double shippingTax;
	private String currency;
	private int quantity;
	private String status;
	private Date timePlaced;
	private Date timeUpdate;
	private String fullfillmentType;
	private String productDesciption;
	private String imageUrl;
	@ManyToOne
	@JoinColumn(unique = true)
	@JsonIgnore
	private Orders orders;
	
	@PrePersist
	protected void onCreate() {
		this.timePlaced = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.timeUpdate = new Date();
	}
}
