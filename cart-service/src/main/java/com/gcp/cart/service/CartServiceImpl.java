package com.gcp.cart.service;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gcp.cart.domain.Orders;
import com.gcp.cart.model.OrderRequest;
import com.gcp.cart.model.Response;
import com.gcp.cart.controller.CartController;
import com.gcp.cart.domain.OrderItems;
import com.gcp.cart.repository.OrderItemsRepository;
import com.gcp.cart.repository.OrdersRepository;
import com.gcp.cart.util.CartServiceConstants;
import com.gcp.cart.util.CartServiceUtil;

/**
 * @author Anuj Kumar
 * 
 *         This service class is to implement the service layer methods to write
 *         the business logic
 */
@Service
public class CartServiceImpl implements CartService {

	private static final Logger logger = LoggerFactory.getLogger(CartController.class);
	
	@Autowired
	protected OrdersRepository ordersRepository;

	@Autowired
	protected OrderItemsRepository orderItemsRepository;

	/**
	 * This method is used to add items into cart
	 * 
	 * @param orderRequest
	 * @return Response
	 */
	@Override
	public Response createOrder(OrderRequest orderRequest) {
		logger.info("Start createOrder method: ", CartServiceImpl.class.getName());
		Response resp = new Response();
		Orders order = new Orders();
		
		// To check if order is already exists or not for guest user
		if (orderRequest.getMemberId() == 0 && orderRequest.getOrderId() == 0) {
			// create new order for guest user
			order = ordersRepository.save(CartServiceUtil.setOrderEntity(orderRequest));
		} else if (orderRequest.getMemberId() == 0 && orderRequest.getOrderId() != 0) {
			// Add new order item in existing order for guest user
			order = ordersRepository.findOrderByOrdersId(orderRequest.getOrderId());
		} else if (orderRequest.getMemberId() != 0 && orderRequest.getOrderId() != 0) {
			// Add new order item in existing order for logined user i.e. cart
			// merge
			order = ordersRepository.findOrderByOrdersId(orderRequest.getOrderId());
			order.setMemberId(orderRequest.getMemberId());
		} else if (orderRequest.getMemberId() != 0 && orderRequest.getOrderId() == 0) {
			// To check if any pending order is already exists or not for
			// logined user
			order = ordersRepository.findPendingOrderByMemberId(orderRequest.getMemberId(),
					CartServiceConstants.PENDING_ORDER);
			if (null == order) {
				// To create new order for logined user
				order = ordersRepository.save(CartServiceUtil.setOrderEntity(orderRequest));
			}
		}	

	if(null!=order)
	{
		// create the order item entry
		OrderItems orderItem = CartServiceUtil.setOrderItemEntity(orderRequest);
		orderItem.setOrders(order);
		orderItem.setMemberId(order.getMemberId());
		orderItem = orderItemsRepository.save(orderItem);

		// To re-calculate order & update the DB
		List<OrderItems> ordItemList = orderItemsRepository.findOrderItemsByOrdersId(order);
		order = ordersRepository.save(CartServiceUtil.recalculateOrder(order, ordItemList));

		// To prepare response with order & order items
		if (order.getId() != 0) {
			order = ordersRepository.findOrderByOrdersId(order.getId());
			resp = CartServiceUtil.prepareFinalOrderResponse(order, ordItemList);
		}
	}
	logger.info("End createOrder method: ", CartServiceImpl.class.getName());
	return resp;
	}

	/**
	 * This method is used update the quantity of order item
	 * 
	 * @param orderItemId
	 * @param quantity
	 * @return Response
	 */
	@Override
	public Response updateOrder(int orderItemId, int quantity) {
		logger.info("Start updateOrder method: ", CartServiceImpl.class.getName());
		Response resp = new Response();
		Orders order = new Orders();

		// Find order item and update the quantity
		OrderItems orderItem = orderItemsRepository.findOrderItemsId(orderItemId);
		double unitPrice = orderItem.getPrice() / orderItem.getQuantity();
		orderItem.setQuantity(quantity);
		orderItem.setPrice(unitPrice * quantity);
		orderItem = orderItemsRepository.save(orderItem);

		// To prepare response with order & order items
		order = ordersRepository.findOrderByOrdersId(orderItem.getOrders().getId());
		List<OrderItems> ordItemList = orderItemsRepository.findOrderItemsByOrdersId(order);

		// To re-calculate order & update the DB
		order = ordersRepository.save(CartServiceUtil.recalculateOrder(order, ordItemList));
		resp = CartServiceUtil.prepareFinalOrderResponse(order, ordItemList);
		logger.info("End updateOrder method: ", CartServiceImpl.class.getName());
		return resp;
	}

	/**
	 * This method is used delete the order item
	 * 
	 * @param orderItemId
	 * @return Response
	 */
	@Override
	public Response deleteOrder(int orderItemId) {
		logger.info("Start deleteOrder method: ", CartServiceImpl.class.getName());
		Response resp = new Response();
		Orders order = new Orders();

		// delete the order item
		OrderItems orderItem = orderItemsRepository.findOrderItemsId(orderItemId);
		orderItemsRepository.delete(orderItem);

		// To check if order has any other items or not, if not then delete the
		// order as well
		List<OrderItems> ordItemList = orderItemsRepository.findOrderItemsByOrdersId(orderItem.getOrders());
		if (ordItemList.size() == 0) {
			// To the order from orders table
			ordersRepository.delete(orderItem.getOrders());
			return null;
		} else {
			// To re-calculate order & update the DB
			order = ordersRepository.findOrderByOrdersId(orderItem.getOrders().getId());
			order = ordersRepository.save(CartServiceUtil.recalculateOrder(order, ordItemList));

			// To prepare response with order & order items
			resp = CartServiceUtil.prepareFinalOrderResponse(order, ordItemList);
		}
		logger.info("End deleteOrder method: ", CartServiceImpl.class.getName());
		return resp;
	}

	/**
	 * This method is used find all the orders of a customer
	 * 
	 * @param memberId
	 * @return List<Response>
	 */
	@Override
	public List<Response> findOrderByMemberId(long memberId) {
		logger.info("Start findOrderByMemberId method: ", CartServiceImpl.class.getName());
		List<Response> finalOrderList = new ArrayList<Response>();
		List<Orders> orderList = ordersRepository.findOrderByMemberId(memberId);
		orderList.parallelStream().forEach(order -> {
			// Find the associated list of order items
			List<OrderItems> ordItemList = orderItemsRepository.findOrderItemsByOrdersId(order);

			// Prepare the response object
			Response resp = CartServiceUtil.prepareFinalOrderResponse(order, ordItemList);
			finalOrderList.add(resp);
		});
		logger.info("End findOrderByMemberId method: ", CartServiceImpl.class.getName());
		return finalOrderList;
	}

	/**
	 * This method is used find all the orders of a customer
	 * 
	 * @param OrderRequest
	 * @return List<Response>
	 */
	@Override
	public Response cartCheckout(OrderRequest orderRequest) {
		logger.info("Start cartCheckout method: ", CartServiceImpl.class.getName());
		Orders order = new Orders();
		// To update the memberId, addressId and status for all order Items and
		// order
		order = ordersRepository.findOrderByOrdersId(orderRequest.getOrderId());
		order.setAddressId(orderRequest.getAddressId());
		order.setMemberId(orderRequest.getMemberId());
		order.setStatus(CartServiceConstants.CONFIRMED_ORDER);
		order = ordersRepository.save(order);
		List<OrderItems> ordItemList = orderItemsRepository.findOrderItemsByOrdersId(order);
		if (!ordItemList.isEmpty()) {
			ordItemList.parallelStream().forEach(orderItem -> {
				orderItem.setAddressId(orderRequest.getAddressId());
				orderItem.setMemberId(orderRequest.getMemberId());
				orderItem.setStatus(CartServiceConstants.CONFIRMED_ORDER);
				orderItemsRepository.saveAll(ordItemList);
			});
		}

		// Prepare the response object
		Response resp = CartServiceUtil.prepareFinalOrderResponse(order, ordItemList);
		logger.info("End cartCheckout method: ", CartServiceImpl.class.getName());
		return resp;
	}
}
