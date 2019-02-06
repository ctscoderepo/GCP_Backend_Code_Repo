package com.gcp.productdetail.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gcp.productdetail.domain.Product;
import com.gcp.productdetail.service.ProductDetailService;

/**
 * @author Anuj Kumar
 * 
 *         This class is rest controller which publish product list end point.
 */

@RestController
@RequestMapping("/api/product-list")
public class ProductListController {

	@Autowired
	ProductDetailService productDetailService;

	/**
	 * This method is used to call the getProductDetails method of
	 * ProductDetailService
	 * 
	 * @return ResponseEntity<Product>
	 */
	@GetMapping(produces = "application/json", path = "/v1")
	public ResponseEntity<List<Product>> getProductList() {
		List<Product> productList = productDetailService.getProductList();
		if (null != productList)
			return new ResponseEntity<List<Product>>(productList, HttpStatus.OK);
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
}
