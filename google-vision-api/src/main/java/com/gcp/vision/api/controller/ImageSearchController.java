package com.gcp.vision.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.JsonNode;
import com.gcp.vision.api.model.FinalResponse;
import com.gcp.vision.api.service.ImageSearchService;

/**
 * @author Anuj Kumar
 * 
 *   This class is the main controller used to define the end points
 */
@CrossOrigin
@RestController
public class ImageSearchController {
	
	private static final Logger logger = LoggerFactory.getLogger(ImageSearchController.class);
	
	@Autowired
	protected ImageSearchService imageSearchService;
	
	/**
	 * This method is used to call service layer methods to search the image and return the product list
	 * 
	 * @param JsonNode
	 * @return ResponseEntity<?>
	 */
	@PostMapping(path = "/vision/searchImage")
	public ResponseEntity<?> getProductsList(@RequestBody JsonNode node) {
		logger.info("Start getProductsList method:", ImageSearchController.class.getName());
		logger.debug("Request json:", node);
		FinalResponse response = imageSearchService.decodeImage(node);	
		logger.debug("Response json:", response);		
				
		if(null!= response.getErrorMessage() && !response.getErrorMessage().isEmpty()){
			return new ResponseEntity<FinalResponse>(response, HttpStatus.SERVICE_UNAVAILABLE);
		}
		logger.info("End getProductsList method:", ImageSearchController.class.getName());
		return new ResponseEntity<FinalResponse>(response, HttpStatus.OK);
	}	
	

	@GetMapping(path = "/vision/check")
	public ResponseEntity<?> checkServiceStatus() {				
		return new ResponseEntity<String>("true", HttpStatus.OK);
	}	
}