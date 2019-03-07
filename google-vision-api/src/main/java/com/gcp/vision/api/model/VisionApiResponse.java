package com.gcp.vision.api.model;

import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * @author Anuj Kumar
 * 
 * This class is used to hold vision api response decoded text
 */
@Data
public class VisionApiResponse {

	private String imageDecodedText;
	private Map<String, List<String>> errorMap;
}
