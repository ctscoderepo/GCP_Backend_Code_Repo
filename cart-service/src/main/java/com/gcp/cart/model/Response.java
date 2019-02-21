package com.gcp.cart.model;

import java.util.Date;
import java.util.List;
import com.gcp.cart.domain.OrderItems;
import lombok.Data;

/**
 * @author Anuj Kumar
 * 
 * This class represent final response which to be consumed by front end
 */
@Data
public class Response {
	private int orderId;
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
	private List<OrderItems> orderItems;
}
