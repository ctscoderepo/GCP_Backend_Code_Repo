package com.gcp.vision.api.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.gcp.vision.api.model.VisionApiResponse;

/**
 * @author Anuj Kumar
 * 
 *   This client is to implement the client factory methods to call vision api and search api 
 */
public interface ImageSearchClient {
	
	/**
	 * This method is used to call the vision api and return decoded text
	 * 
	 * @param JsonNode
	 * @return VisionApiResponse
	 */
	VisionApiResponse getTextForImage(JsonNode node) throws Exception;
		
	/**
	 * This method is used to call the search service to get the product list based on decoded text
	 * 
	 * @param decodedText
	 * @return String
	 */
	String searchByDecodedText(String decodedText) throws Exception;
}
