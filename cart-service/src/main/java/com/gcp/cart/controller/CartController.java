package com.gcp.cart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gcp.cart.domain.Order;
import com.gcp.cart.service.CartService;

@CrossOrigin
@RestController
@RequestMapping("/api/shoppingcart/add")
public class CartController {
	
	@Autowired CartService cartService;
	
	@PostMapping
	public ResponseEntity<?> addItemToCart(@RequestBody Order order){
		return new ResponseEntity<Order>(cartService.createOrder(order), HttpStatus.CREATED);
	}

}
