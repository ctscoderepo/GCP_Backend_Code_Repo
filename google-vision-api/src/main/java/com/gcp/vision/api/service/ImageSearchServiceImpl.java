package com.gcp.vision.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcp.vision.api.client.ImageSearchClient;
import com.gcp.vision.api.model.FinalResponse;
import com.gcp.vision.api.model.VisionApiResponse;
import com.gcp.vision.api.util.ImageSearchConstants;

/**
 * @author Anuj Kumar
 * 
 *         This service class is to implement the service layer methods to write
 *         the business logic
 */
@Service
public class ImageSearchServiceImpl implements ImageSearchService {

	private static final Logger logger = LoggerFactory.getLogger(ImageSearchServiceImpl.class);

	@Autowired
	ImageSearchClient imageSearchClient;

	/**
	 * This method is used to call the client layer to interact with vision api
	 * and prepare the final response
	 * 
	 * @param JsonNode
	 * @return FinalResponse
	 */
	@Override
	public FinalResponse decodeImage(JsonNode node) {
		logger.info("Start decodeImage method:", ImageSearchServiceImpl.class.getName());
		logger.info("Request json:", node);
		FinalResponse response = new FinalResponse();
		try {
			//Call the vision api client to get the decoded text for image
			VisionApiResponse apiResponse = imageSearchClient.getTextForImage(node);
			logger.info("Vision api response:", apiResponse);
			
			//Call the search service to get the product list based on decoded text
			if (null != apiResponse && !apiResponse.getImageDecodedText().isEmpty()) {
				
				String searchServiceResp = imageSearchClient.searchByDecodedText(apiResponse.getImageDecodedText());
				logger.info("Search service response:", searchServiceResp);
				if (null != searchServiceResp && !searchServiceResp.isEmpty()) {
					byte[] jsonData = searchServiceResp.getBytes("utf-8");
					ObjectMapper objectMapper = new ObjectMapper();
					JsonNode rootNode = objectMapper.readTree(jsonData);
					response.setProducts(rootNode);
				}
			}
			response.setMessage(ImageSearchConstants.SUCCESS_RESPONSE);
			logger.info("Search service response:", response);
			
		} catch (Exception ex) {
			response.setMessage(ImageSearchConstants.ERROR_RESPONSE);
			response.setErrorMessage(ex.getMessage());
			logger.info("Exception:", ex.getMessage());			
		}		
		logger.info("End decodeImage method:", ImageSearchServiceImpl.class.getName());
		return response;
	}

}
