package com.gcp.vision.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcp.vision.api.client.ImageSearchClient;
import com.gcp.vision.api.model.FinalResponse;
import com.gcp.vision.api.model.VisionApiResponse;

@Service
public class ImageSearchServiceImpl implements ImageSearchService {

	@Autowired
	ImageSearchClient imageSearchClient;

	@Override
	public FinalResponse decodeImage(JsonNode node) {
		FinalResponse response = new FinalResponse();
		try {
			VisionApiResponse apiResponse = imageSearchClient.getTextForImage(node);

		/*	if (!apiResponse.getErrorMap().isEmpty()) {
				response.setErrorMap(apiResponse.getErrorMap());
				return response;
			}*/

			// Call the search service to get the product list based on decoded
			// text
			if (null != apiResponse && !apiResponse.getImageDecodedText().isEmpty()) {
				String searchServiceResp = imageSearchClient.searchByDecodedText(apiResponse.getImageDecodedText());
				if (null != searchServiceResp && !searchServiceResp.isEmpty()) {
					byte[] jsonData = searchServiceResp.getBytes("utf-8");
					ObjectMapper objectMapper = new ObjectMapper();
					JsonNode rootNode = objectMapper.readTree(jsonData);
					response.setProducts(rootNode);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

}
