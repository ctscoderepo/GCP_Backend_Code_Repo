package com.gcp.productdetail.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.gcp.productdetail.domain.Product;

/**
 * @author Anuj Kumar
 * 
 *  This class to have common util methods
 */
public class ProductCommonUtil {

	/**
	 * This method get the product details from json object
	 * 
	 * @return Product
	 */
	public static Product getProduct(JsonNode node) {
		Product product = new Product();
		product.setProductName(node.get("productName").asText());
		product.setSkuId(node.get("skuId").asText());
		product.setShortDescription(node.get("shortDescription").asText());
		product.setLongtDescription(node.get("longtDescription").asText());
		product.setStockQuantity(node.get("stockQuantity").asText());
		product.setPrice(node.get("price").asText());
		product.setCurrency(node.get("currency").asText());
		product.setImageUrl(node.get("imageUrl").asText());
		product.setCategory(node.get("category").asText());
		product.setWeight(node.get("weight").asText());
		product.setLength(node.get("length").asText());
		product.setWidth(node.get("width").asText());
		product.setHeight(node.get("height").asText());
		return product;
	}
}
