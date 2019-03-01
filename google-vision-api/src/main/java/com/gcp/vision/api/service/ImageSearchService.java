package com.gcp.vision.api.service;

import com.gcp.vision.api.model.FinalResponse;

public interface ImageSearchService {
	FinalResponse decodeImage(String imageUrl);

}
