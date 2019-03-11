package com.gcp.cart.model;

import java.util.List;
import lombok.Data;

/**
 * @author Anuj Kumar
 * 
 * This class represent orders list response
 */
@Data
public class OrdersResponse {
	private String errorMessage;
	private String message;
	private List<OrderDetail> ordersList;
}
