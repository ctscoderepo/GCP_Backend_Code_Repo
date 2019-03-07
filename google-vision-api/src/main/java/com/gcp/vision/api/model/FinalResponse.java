package com.gcp.vision.api.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

/**
 * @author Anuj Kumar
 * 
 * This class is used to hold FinalResponse service
 */
@Data
public class FinalResponse {
	private JsonNode products;
	private String errorMessage;
	private String message;
}
