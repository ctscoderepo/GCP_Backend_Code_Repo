package com.gcp.vision.api.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.gcp.vision.api.model.FinalResponse;

public interface ImageSearchService {
	FinalResponse decodeImage(JsonNode node);

}
