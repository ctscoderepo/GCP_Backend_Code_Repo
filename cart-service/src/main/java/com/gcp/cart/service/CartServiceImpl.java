package com.gcp.cart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gcp.cart.domain.Order;
import com.gcp.cart.repository.CartRepository;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	protected CartRepository cartRepository;

	@Override
	public Order createOrder(Order order) {
		Order response = cartRepository.save(order);
		return response;
	}

}
