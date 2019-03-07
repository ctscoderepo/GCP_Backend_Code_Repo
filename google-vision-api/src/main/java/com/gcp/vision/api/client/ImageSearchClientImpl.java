package com.gcp.vision.api.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.gcp.vision.api.model.LabelAnnotations;
import com.gcp.vision.api.model.Response;
import com.gcp.vision.api.model.VisionApiResponse;
import com.gcp.vision.api.util.ImageSearchConstants;
import com.google.api.client.util.Value;
import java.net.URI;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author Anuj Kumar
 * 
 *         This client is to implement the client factory methods to call vision
 *         api and search api
 */
@Component
public class ImageSearchClientImpl implements ImageSearchClient {

	private static final Logger logger = LoggerFactory.getLogger(ImageSearchClientImpl.class);

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Value(ImageSearchConstants.SEARCH_SERVICE_URI)
	private String searchServiceUrl;

	@Value(ImageSearchConstants.VISION_API_KEY)
	private String visionApiKey;

	@Value(ImageSearchConstants.VISION_API_URI)
	private String visionApiUri;

	/**
	 * This method is used to call the vision api and return decoded text
	 * 
	 * @param JsonNode
	 * @return VisionApiResponse
	 */
	public VisionApiResponse getTextForImage(JsonNode node) throws Exception {
		logger.info("Start getTextForImage method:", ImageSearchClientImpl.class.getName());
		logger.debug("Request json:", node);
		ResponseEntity<Response> visionApiResp;
		VisionApiResponse apiResponse = new VisionApiResponse();
		
		URI visionApiUrl = new URI(visionApiUri+visionApiKey);
		
		//URI visionApiUrl = new URI("https://vision.googleapis.com/v1/images:annotate?key=AIzaSyBu0GUKiUnrc20TGT2I4WJxV25oqPOYf7g");
		logger.debug("Vision api end poin url: " + visionApiUrl);
		visionApiResp = restTemplate().postForEntity(visionApiUrl, node, Response.class);
		logger.debug("Vision api response: " + visionApiResp.getBody());
		Response resposne = visionApiResp.getBody();

		List<LabelAnnotations> labelAnnotationList = resposne.getResponses();
		LabelAnnotations labelAnnotation = labelAnnotationList.get(0);

		// To find the highest score matched imaged text
		labelAnnotation.getLabelAnnotations().parallelStream().forEach(textLabel -> {
			double score = 0.0;
			if (textLabel.getScore() > score) {
				score = textLabel.getScore();
				apiResponse.setImageDecodedText(textLabel.getDescription());
			}
		});

		logger.debug("Descoded text response: " + apiResponse);
		logger.info("End getTextForImage method:", ImageSearchClientImpl.class.getName());
		return apiResponse;
	}

	/**
	 * This method is used to call the search service to get the product list
	 * based on decoded text
	 * 
	 * @param decodedText
	 * @return String
	 */
	@Override
	public String searchByDecodedText(String decodedText) throws Exception {
		logger.info("Start searchByDecodedText method:", ImageSearchClientImpl.class.getName());
		logger.debug("DecodedText:", decodedText);
		
		// To call the search service to search decoded image text
		searchServiceUrl = searchServiceUrl + decodedText;
		//searchServiceUrl = "http://104.154.92.99/keywordsearch?keyword=" + decodedText;
		logger.debug("Search service end point url: " + searchServiceUrl);
		String result = restTemplate().getForObject(searchServiceUrl, String.class);
		logger.debug("Search service response: " + result);
		logger.info("End searchByDecodedText method:", ImageSearchClientImpl.class.getName());
		return result;
	}

}