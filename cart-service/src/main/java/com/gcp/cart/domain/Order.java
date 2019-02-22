package com.gcp.cart.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private long memberId;
	private long addressId;
	private double totalPrice;
	private double totalShipping;
	private double totalTax;
	private double totalShippingTax;
	private String currency;
	private String status;
	private Date timePlaced;
	private Date timeUpdate;
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> orderItems;
	
	@PrePersist
	protected void onCreate() {
		this.timePlaced = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.timeUpdate = new Date();
	}
}
