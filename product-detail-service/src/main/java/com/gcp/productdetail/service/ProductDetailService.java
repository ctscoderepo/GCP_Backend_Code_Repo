package com.gcp.productdetail.service;

import java.util.List;
import com.gcp.productdetail.domain.Product;

/**
 * @author Anuj Kumar
 * 
 *  This service declared to have getProductDetail & getProductList methods
 */
public interface ProductDetailService {	
	
	Product getProductDetail(String skuId);
	
	List<Product> getProductList();
}
