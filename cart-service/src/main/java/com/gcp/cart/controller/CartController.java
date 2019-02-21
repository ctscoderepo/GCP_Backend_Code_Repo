package com.gcp.cart.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gcp.cart.model.OrderRequest;
import com.gcp.cart.model.Response;
import com.gcp.cart.service.CartService;

/**
 * @author Anuj Kumar
 * 
 * This class main controller which have all the end points exposed for cart service
 */
@CrossOrigin
@RestController
@RequestMapping("/api")
public class CartController {
	private static final Logger logger = LoggerFactory.getLogger(CartController.class);
	
	@Autowired CartService cartService;
	
	/**
	 * This end point is used to add items into cart
	 * 
	 * @param OrderRequest
	 * @return ResponseEntity<?>
	 */
	@PostMapping(path = "/shoppingcart/add")
	public ResponseEntity<?> addItemToCart(@RequestBody OrderRequest orderRequest){
		logger.info("Start addItemToCart method: ", CartController.class.getName());
		logger.debug("OrderRequest: "+orderRequest);
		Response resp = cartService.createOrder(orderRequest);
		logger.debug("Response: "+resp);
		logger.info("End addItemToCart method: ", CartController.class.getName());
		return new ResponseEntity<Response>(resp, HttpStatus.CREATED);
	}
	
	/**
	 * This end point is used to get list of orders of a customer
	 * 
	 * @param memberId
	 * @return ResponseEntity<?>
	 */
	@GetMapping(path = "/order/{memberId}")
	public ResponseEntity<?> getOrdersByMemberId(@PathVariable long memberId){
		logger.info("Start getOrdersByMemberId method: ", CartController.class.getName());
		logger.debug("OrderRequest: "+memberId);
		List<Response> resp = cartService.findOrderByMemberId(memberId);
		logger.debug("Response: "+resp);
		logger.info("End getOrdersByMemberId method: ", CartController.class.getName());
		return new ResponseEntity<List<Response>>(resp, HttpStatus.OK);
	}
	
	/**
	 * This end point is used to update the quantity of an order item
	 * 
	 * @param OrderRequest
	 * @return ResponseEntity<?>
	 */
	@PostMapping(path = "/shoppingcart/update")
	public ResponseEntity<?> updateOrderItemQty(@RequestBody OrderRequest orderRequest){
		logger.info("Start updateOrderItemQty method: ", CartController.class.getName());
		logger.debug("OrderRequest: "+orderRequest);
		Response resp = cartService.updateOrder(orderRequest.getOrderItemsId(), orderRequest.getQuantity());
		logger.debug("Response: "+resp);
		logger.info("End updateOrderItemQty method: ", CartController.class.getName());
		return new ResponseEntity<Response>(resp, HttpStatus.CREATED);
	}
	
	/**
	 * This end point is used to delete order item/order
	 * 
	 * @param orderItemId
	 * @return ResponseEntity<?>
	 */
	@DeleteMapping(path = "/shoppingcart/delete/{orderItemId}")
	public ResponseEntity<?> deleteOrder(@PathVariable String orderItemId){
		logger.info("Start deleteOrder method: ", CartController.class.getName());
		logger.debug("orderItemId: "+orderItemId);
		Response resp = cartService.deleteOrder(Integer.parseInt(orderItemId));
		logger.debug("Response: "+resp);
		logger.info("End updateOrderItemQty method: ", CartController.class.getName());
		return new ResponseEntity<Response>(resp, HttpStatus.CREATED);
	}
	
	/**
	 * This end point is used for final cart checkout
	 * 
	 * @param OrderRequest
	 * @return ResponseEntity<?>
	 */
	@PostMapping(path = "/shoppingcart/checkout")
	public ResponseEntity<?> cartCheckout(@RequestBody OrderRequest orderRequest){
		logger.info("Start cartCheckout method: ", CartController.class.getName());
		logger.debug("OrderRequest: "+orderRequest);
		Response resp = cartService.cartCheckout(orderRequest);
		logger.debug("Response: "+resp);
		logger.info("End cartCheckout method: ", CartController.class.getName());
		return new ResponseEntity<Response>(resp, HttpStatus.CREATED);
	}
}
