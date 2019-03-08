package com.gcp.cart.service;

import org.springframework.stereotype.Service;
import com.gcp.cart.model.OrderRequest;
import com.gcp.cart.model.OrdersResponse;
import com.gcp.cart.model.Product;
import com.gcp.cart.model.Response;

/**
 * @author Anuj Kumar
 * 
 *         This service class is to implement the service layer methods to write
 *         the business logic
 */
@Service
public interface CartService {
	
	/**
	 * This method is used to add items into cart
	 * 
	 * @param orderRequest
	 * @return Response
	 */
	Response createOrder(OrderRequest orderRequest);
	
	/**
	 * This method is used update the quantity of order item
	 * 
	 * @param orderItemId
	 * @param quantity
	 * @return Response
	 */
	Response updateOrder(int orderItemId, int quantity);
	
	/**
	 * This method is used delete the order item
	 * 
	 * @param orderItemId
	 * @return Response
	 */
	Response deleteOrder(int orderItemId);

	/**
	 * This method is set the order item entity
	 * 
	 * @param OrderRequest
	 * @return OrdersResponse
	 */
	OrdersResponse findOrderByMemberId(long memberId);
	
	/**
	 * This method is used for final checkout
	 * 
	 * @param orderRequest
	 * @return Response
	 */
	Response cartCheckout(OrderRequest orderRequest);
	
	/**
	 * This method is used for final checkout
	 * 
	 * @param skuId
	 * @return Product
	 */
	Product getProductDetails(String skuId) throws Exception;
}
