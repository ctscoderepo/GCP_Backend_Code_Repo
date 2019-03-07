package com.gcp.vision.api.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.gcp.vision.api.model.FinalResponse;

/**
 * @author Anuj Kumar
 * 
 *  This service is to declare the service operations 
 *        
 */
public interface ImageSearchService {
	
	/**
	 * This method is used to call the client layer to interact with vision api
	 * 
	 * @param JsonNode
	 * @return FinalResponse
	 */
	FinalResponse decodeImage(JsonNode node);
}
