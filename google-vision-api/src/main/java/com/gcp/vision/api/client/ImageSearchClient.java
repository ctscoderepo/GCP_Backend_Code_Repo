package com.gcp.vision.api.client;

import com.gcp.vision.api.model.VisionApiResponse;

public interface ImageSearchClient {
	VisionApiResponse getDecodedTextResponse(String imageUrl) throws Exception;
	
	String searchByDecodedText(String decodedTest);
}
