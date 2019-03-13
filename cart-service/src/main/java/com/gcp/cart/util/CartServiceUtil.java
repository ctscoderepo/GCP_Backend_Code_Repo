package com.gcp.cart.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gcp.cart.controller.CartController;
import com.gcp.cart.domain.OrderItems;
import com.gcp.cart.domain.Orders;
import com.gcp.cart.model.OrderDetail;
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
		resp.setDiscount(order.getDiscount());

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
		if (totalPrice > 100.00) {
			totalShippingCharge = CartServiceConstants.FREE_SHIPPING;
			totalShippingTax = 0.00;
			order.setDiscount(CartServiceConstants.SHIPPING_CHARGE);
		} else {
			totalShippingCharge = CartServiceConstants.SHIPPING_CHARGE;
			totalShippingTax = Math.round(totalShippingCharge * CartServiceConstants.SHIPPING_TAX_RATE) / 100;
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
		order.setTotalPrice(Math.round(orderRequest.getPrice() * orderRequest.getQuantity()));
		order.setTotalTax(Math.round(orderRequest.getPrice() * CartServiceConstants.TAX_RATE) / 100);
		if (orderRequest.getPrice() < 100)
			order.setTotalShipping(CartServiceConstants.SHIPPING_CHARGE);
		else {
			order.setTotalShipping(CartServiceConstants.FREE_SHIPPING);
			order.setDiscount(CartServiceConstants.SHIPPING_CHARGE);
		}

		order.setTotalShippingTax(Math.round(order.getTotalShipping() * CartServiceConstants.SHIPPING_TAX_RATE) / 100);
		order.setCurrency(CartServiceConstants.CURRENCY);
		order.setStatus(CartServiceConstants.PENDING_ORDER);
		order.setMemberId(orderRequest.getMemberId());
		order.setEmail(orderRequest.getEmail());
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
		orderItem.setShippingTax(
				Math.round(orderItem.getShippingCharge() * CartServiceConstants.SHIPPING_TAX_RATE) / 100);
		orderItem.setCurrency(CartServiceConstants.CURRENCY);
		orderItem.setStatus(CartServiceConstants.PENDING_ORDER);
		orderItem.setFullfillmentType(CartServiceConstants.FULLFILLMENT_TYPE);
		orderItem.setMemberId(orderRequest.getMemberId());
		logger.info("End setOrderItemEntity method: ", CartServiceUtil.class.getName());
		return orderItem;
	}

	/**
	 * This method is validate the order request
	 * 
	 * @param OrderRequest
	 * @param endPoint
	 * @return Map<String, String>
	 */
	public static Map<String, String> validateRequest(OrderRequest orderRequest, String endPoint) {
		Map<String, String> errorMap = new HashMap<String, String>();
		if (endPoint.equals("add")) {
			if (null == orderRequest.getProductId() || orderRequest.getProductId().isEmpty()) {
				errorMap.put("errorMessage", "ProductId is required.");
			}
			if (orderRequest.getPrice() == 0) {
				errorMap.put("errorMessage", "Price is required.");
			}
			if (orderRequest.getQuantity() == 0) {
				errorMap.put("errorMessage", "Quantity is required.");
			}
		}
		if (endPoint.equals("update")) {
			if (orderRequest.getOrderItemsId() == 0) {
				errorMap.put("errorMessage", "orderItemId is required.");
			}
			if (orderRequest.getQuantity() == 0) {
				errorMap.put("errorMessage", "Quantity is required.");
			}
		}
		if (endPoint.equals("checkout")) {
			if (orderRequest.getOrderId() == 0) {
				errorMap.put("errorMessage", "orderId is required.");
			}
			if (orderRequest.getMemberId() == 0) {
				if (null == orderRequest.getEmail() || orderRequest.getEmail().isEmpty())
					errorMap.put("errorMessage", "memberId or email is required.");
			}
			if (orderRequest.getMemberId() != 0 && orderRequest.getAddressId() == 0) {
				errorMap.put("errorMessage", "addressId is required.");
			}
		}

		return errorMap;
	}

	/**
	 * This method is validate the request param
	 * 
	 * @param requestParam
	 * @param endPoint
	 * @return Map<String, String>
	 */
	public static Map<String, String> validateRequestParam(String requestParam, String endPoint) {
		Map<String, String> errorMap = new HashMap<String, String>();
		if (endPoint.equals("delete")) {
			if (requestParam.isEmpty()) {
				errorMap.put("errorMessage", "OrderItemId is required.");
			}
		}
		if (endPoint.equals("byMemberId")) {
			if (requestParam.isEmpty()) {
				errorMap.put("errorMessage", "MemberId is required.");
			}
		}
		if (endPoint.equals("byOrderId")) {
			if (requestParam.isEmpty()) {
				errorMap.put("errorMessage", "OrderId is required.");
			}
		}
		return errorMap;
	}

	/**
	 * This method is used to prepare the final response
	 * 
	 * @param Orders
	 * @param List<OrderItems
	 * @return Response
	 */
	public static OrderDetail prepareFinalOrderListResponse(Orders order, List<OrderItems> ordItemList) {
		logger.info("Start prepareFinalOrderResponse method: ", CartServiceUtil.class.getName());
		OrderDetail orderDetail = new OrderDetail();
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
		orderDetail.setOrderId(order.getId());
		orderDetail.setMemberId(order.getMemberId());
		orderDetail.setAddressId(order.getAddressId());
		orderDetail.setTotalPrice(order.getTotalPrice());
		orderDetail.setTotalShipping(order.getTotalShipping());
		orderDetail.setTotalTax(order.getTotalTax());
		orderDetail.setTotalShippingTax(order.getTotalShippingTax());
		orderDetail.setCurrency(order.getCurrency());
		orderDetail.setStatus(order.getStatus());
		orderDetail.setTimePlaced(order.getTimePlaced());
		orderDetail.setTimeUpdate(order.getTimeUpdate());
		orderDetail.setDiscount(order.getDiscount());

		// To set the order items in response
		orderDetail.setOrderItems(tmpItemList);
		logger.info("End prepareFinalOrderResponse method: ", CartServiceUtil.class.getName());
		return orderDetail;
	}
}
