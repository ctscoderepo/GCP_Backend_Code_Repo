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
}
