package com.gcp.productdetail.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcp.productdetail.domain.Product;
import com.gcp.productdetail.util.ProductCommonUtil;

/**
 * @author Anuj Kumar
 * 
 *         This service declared to have getProductDetail & getProductList
 *         method implementation
 */
@Service
public class ProductDetailServiceImpl implements ProductDetailService {

	/**
	 * This method has implementation of getProductDetail to return product
	 * details
	 * 
	 * @param skuId
	 * @return Product
	 */
	public Product getProductDetail(String skuId) {
		Product product = new Product();

		try {
			String fileName = "static/ProductList.txt";
			/*
			 * ClassLoader classLoader = new
			 * ProductDetailServiceImpl().getClass().getClassLoader(); File file
			 * = new File(classLoader.getResource(fileName).getFile());
			 */
			// File file = ResourceUtils.getFile(fileName);
			// byte[] jsonData = Files.readAllBytes(file.toPath());
			//using to read inputStream 
			byte[] byteArray = new byte[4024];
			InputStream inputStream = new ClassPathResource(fileName).getInputStream();
			inputStream.read(byteArray);			
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(byteArray);
			JsonNode productsNode = rootNode.path("productList");

			Iterator<JsonNode> prdItr = productsNode.elements();
			prdItr.forEachRemaining(node -> {
				prdItr.next();
				if (!node.get("skuId").asText().isEmpty() && node.get("skuId").asText().equals(skuId)) {
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
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	}

	/**
	 * This method has implementation of getProductList to return product list
	 *
	 * @return List<Product
	 */
	public List<Product> getProductList() {
		List<Product> productList = new ArrayList<Product>();
		try {
			String fileName = "static/ProductList.txt";
			/*
			 * ClassLoader classLoader = new
			 * ProductDetailServiceImpl().getClass().getClassLoader(); File file
			 * = new File(classLoader.getResource(fileName).getFile());
			 * 
			 * byte[] jsonData = Files.readAllBytes(file.toPath());
			 */
			//using to read inputStream 
			byte[] byteArray = new byte[4024];
			InputStream inputStream = new ClassPathResource(fileName).getInputStream();		
			inputStream.read(byteArray);
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(byteArray);
			JsonNode productsNode = rootNode.path("productList");

			Iterator<JsonNode> prdItr = productsNode.elements();
			prdItr.forEachRemaining(node -> {
				prdItr.next();
				productList.add(ProductCommonUtil.getProduct(node));
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
		return productList;
	}
}
