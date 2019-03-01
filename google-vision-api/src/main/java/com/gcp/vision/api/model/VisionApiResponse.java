package com.gcp.vision.api.model;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class VisionApiResponse {

	private String imageDecodedText;
	private Map<String, List<String>> errorMap;
}
