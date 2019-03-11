package com.gcp.cart.util;

/**
 * @author Anuj Kumar
 * 
 *  This is keep all the constants
 */
public class CartServiceConstants {
	
	public static final int TAX_RATE = 7;
	public static final double SHIPPING_CHARGE = 10.00;
	public static final double FREE_SHIPPING = 0.00;
	public static final int SHIPPING_TAX_RATE = 2;
	public static final String PENDING_ORDER = "P";
	public static final String CONFIRMED_ORDER = "C";
	public static final String CURRENCY = "USD";
	public static final String FULLFILLMENT_TYPE = "ShipToHome";
	public static final String SEARCH_SERVICE_URL = "${search.service.url}";
	public static final String ERROR_RESPONSE = "Service unavailable.";
	public static final String ORDER_DELETED = "No more items in order hence order is deleted";
	public static final String ORDERITEM_DELETED = "Order item is deleted";
	public static final String ORDERITEM_ADDED = "Item added into cart";
	public static final String ORDERITEM_UPDATED = "Item updated";
	public static final String ORDER_PLACED = "Order submitted successfully";
	public static final String ORDER_DETAILS = "Order details";
	public static final String ORDERS_LIST = "Listed are orders for customer";
	public static final String ORDER_NOT_FOUND = "No matching order found.";
}