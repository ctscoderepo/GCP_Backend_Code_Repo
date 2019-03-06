package com.gcp.vision.api.client;

import java.net.URISyntaxException;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;
import com.gcp.vision.api.model.VisionApiResponse;

public interface ImageSearchClient {
	VisionApiResponse getDecodedTextResponse(String imageUrl) throws Exception;
	
	String searchByDecodedText(String decodedTest);
	
	
	VisionApiResponse getTextForImage(JsonNode node) throws URISyntaxException;

}
