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
	private List<Response> ordersList;
	private String errorMessage;
}
