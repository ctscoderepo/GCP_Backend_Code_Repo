package com.gcp.cart.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gcp.cart.controller.CartController;
import com.gcp.cart.domain.OrderItems;
import com.gcp.cart.domain.Orders;
import com.gcp.cart.model.OrderRequest;
import com.gcp.cart.model.Response;

/**
 * @author Anuj Kumar
 * 
 *         This util class to create common business logic being used by service
 *         layer
 */
public class CartServiceUtil {
	private static final Logger logger = LoggerFactory.getLogger(CartController.class);
	/**
	 * This method is used to prepare the final response
	 * 
	 * @param Orders
	 * @param List<OrderItems
	 * @return Response
	 */
	public static Response prepareFinalOrderResponse(Orders order, List<OrderItems> ordItemList) {
		logger.info("Start prepareFinalOrderResponse method: ", CartServiceUtil.class.getName());		
		Response resp = new Response();
		List<OrderItems> tmpItemList = new ArrayList<>();

		// To make the orders field null in order item
		if (!ordItemList.isEmpty()) {
			ordItemList.parallelStream().forEach(orderItem -> {
				OrderItems item = orderItem;
				item.setOrders(null);
				tmpItemList.add(item);
			});
		}
		// To set the orders details in response
		resp.setOrderId(order.getId());
		resp.setMemberId(order.getMemberId());
		resp.setAddressId(order.getAddressId());
		resp.setTotalPrice(order.getTotalPrice());
		resp.setTotalShipping(order.getTotalShipping());
		resp.setTotalTax(order.getTotalTax());
		resp.setTotalShippingTax(order.getTotalShippingTax());
		resp.setCurrency(order.getCurrency());
		resp.setStatus(order.getStatus());
		resp.setTimePlaced(order.getTimePlaced());
		resp.setTimeUpdate(order.getTimeUpdate());

		// To set the order items in response
		resp.setOrderItems(tmpItemList);
		logger.info("End prepareFinalOrderResponse method: ", CartServiceUtil.class.getName());
		return resp;
	}

	/**
	 * This method is to re-calculate the order
	 * 
	 * @param Orders
	 * @param List<OrderItems
	 * @return Orders
	 */
	public static Orders recalculateOrder(Orders order, List<OrderItems> ordItemList) {
		logger.info("Start recalculateOrder method: ", CartServiceUtil.class.getName());
		double totalPrice = 0.0, totalShippingCharge = 0.0, totalTax = 0.0, totalShippingTax = 0.0;

		// To re-calculate the order amount
		if (!ordItemList.isEmpty()) {
			Iterator<OrderItems> itr = ordItemList.iterator();
			while (itr.hasNext()) {
				OrderItems item = itr.next();
				totalPrice += item.getPrice();
				totalShippingCharge += item.getShippingCharge();
				totalTax += item.getTax();
				totalShippingTax += item.getShippingTax();
			}
		}
		order.setTotalPrice(Math.round(totalPrice));
		if(totalPrice>100.00){
			totalShippingCharge = CartServiceConstants.FREE_SHIPPING;
			totalShippingTax = 0.00;
		}else{
			totalShippingCharge = CartServiceConstants.SHIPPING_CHARGE;
			totalShippingTax = Math.round(totalShippingCharge * CartServiceConstants.SHIPPING_TAX_RATE)/100;
		}
		order.setTotalShipping(totalShippingCharge);
		order.setTotalTax(totalTax);
		order.setTotalShippingTax(totalShippingTax);
		logger.info("End recalculateOrder method: ", CartServiceUtil.class.getName());
		return order;
	}

	/**
	 * This method is set the order entity
	 * 
	 * @param OrderRequest
	 * @return Orders
	 */
	public static Orders setOrderEntity(OrderRequest orderRequest) {
		logger.info("Start setOrderEntity method: ", CartServiceUtil.class.getName());
		Orders order = new Orders();
		order.setTotalPrice(Math.round(orderRequest.getPrice()*orderRequest.getQuantity()));
		order.setTotalTax(Math.round(orderRequest.getPrice() * CartServiceConstants.TAX_RATE) / 100);
		if (orderRequest.getPrice() < 100)
			order.setTotalShipping(CartServiceConstants.SHIPPING_CHARGE);
		else
			order.setTotalShipping(CartServiceConstants.FREE_SHIPPING);
		order.setTotalShippingTax(Math.round(order.getTotalShipping() * CartServiceConstants.SHIPPING_TAX_RATE) / 100);
		order.setCurrency(CartServiceConstants.CURRENCY);
		order.setStatus(CartServiceConstants.PENDING_ORDER);
		order.setMemberId(orderRequest.getMemberId());
		logger.info("End setOrderEntity method: ", CartServiceUtil.class.getName());
		return order;
	}

	/**
	 * This method is set the order item entity
	 * 
	 * @param OrderRequest
	 * @return OrderItems
	 */
	public static OrderItems setOrderItemEntity(OrderRequest orderRequest) {
		logger.info("Start setOrderItemEntity method: ", CartServiceUtil.class.getName());
		OrderItems orderItem = new OrderItems();
		orderItem.setProductId(orderRequest.getProductId());
		orderItem.setQuantity(orderRequest.getQuantity());
		orderItem.setPrice(Math.round(orderRequest.getPrice() * orderRequest.getQuantity()));
		orderItem.setTax(Math.round(orderRequest.getPrice() * CartServiceConstants.TAX_RATE) / 100);
		if (orderRequest.getPrice() < 100)
			orderItem.setShippingCharge(CartServiceConstants.SHIPPING_CHARGE);
		else
			orderItem.setShippingCharge(CartServiceConstants.FREE_SHIPPING);
		orderItem.setShippingTax(Math.round(orderItem.getShippingCharge() * CartServiceConstants.SHIPPING_TAX_RATE) / 100);
		orderItem.setCurrency(CartServiceConstants.CURRENCY);
		orderItem.setStatus(CartServiceConstants.PENDING_ORDER);
		orderItem.setMemberId(orderRequest.getMemberId());
		logger.info("End setOrderItemEntity method: ", CartServiceUtil.class.getName());
		return orderItem;
	}

}
