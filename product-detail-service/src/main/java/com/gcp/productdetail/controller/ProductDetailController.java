package com.gcp.productdetail.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.gcp.productdetail.domain.Product;
import com.gcp.productdetail.service.ProductDetailService;

/**
 * @author Anuj Kumar
 * 
 *         This class is rest controller which publish product detail end point.
 */

@RestController
@CrossOrigin
@RequestMapping("/api/product-detail")
public class ProductDetailController {

	@Autowired
	ProductDetailService productDetailService;

	/**
	 * This method is used to call the getProductDetails method of
	 * ProductDetailService
	 * 
	 * @param skuId
	 * @return ResponseEntity<Product>
	 */
	@GetMapping(produces = "application/json", path = "/v1")
	public ResponseEntity<?> getProductList(@Valid @RequestParam(required = true) String skuId) {

		if (null != skuId && !skuId.isEmpty()) {
			Product productDetail = productDetailService.getProductDetail(skuId.toUpperCase());
			if (null != productDetail)
				return new ResponseEntity<Product>(productDetail, HttpStatus.OK);
			else
				return new ResponseEntity<String>("Product not found", HttpStatus.OK);
		} else
			return new ResponseEntity<String>("Request parameter skuId is required", HttpStatus.BAD_REQUEST);
	}
}
