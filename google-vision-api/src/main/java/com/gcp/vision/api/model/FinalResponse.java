package com.gcp.vision.api.model;

import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class FinalResponse {
	JsonNode products;
	Map<String, List<String>> errorMap;
}
