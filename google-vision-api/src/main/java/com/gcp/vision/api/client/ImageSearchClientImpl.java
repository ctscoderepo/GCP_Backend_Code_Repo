package com.gcp.vision.api.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcp.vision.api.model.VisionApiResponse;
import com.google.api.client.util.Value;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.ComputeEngineCredentials;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class ImageSearchClientImpl implements ImageSearchClient{
	
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
	
	@Value("${search.service.url}")
	private String searchServiceUrl;
	
	@Value("${google.vision.api.key}")
	private String visionApiKey;
	
	@Value("${google.vision.api.uri}")
	private String visionApiUri;
	
	public VisionApiResponse getDecodedTextResponse(String imageUrl) throws Exception {		 
		
		VisionApiResponse apiResponse = new VisionApiResponse();
		// Instantiates a client
		try (ImageAnnotatorClient vision = ImageAnnotatorClient.create(setImageAnnotator())) {

			// The path to the image file to annotate
			// String fileName = "C:/Users/756896/Desktop/GCPWork/images/electronics/0000035_nikon-d5500-dslr_550.jpeg";			
			// Reads the image file into memory
			
			Path path = Paths.get(imageUrl);
			byte[] data = Files.readAllBytes(path);
			
			
			ByteString imgBytes = ByteString.copyFrom(data);

			// Builds the image annotation request
			List<AnnotateImageRequest> requests = new ArrayList<>();
			Image img = Image.newBuilder().setContent(imgBytes).build();
			Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
			AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
			requests.add(request);

			// Performs label detection on the image file
			BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
			List<AnnotateImageResponse> responses = response.getResponsesList();
			List<String> errorMsgList = new ArrayList<String>();
			Map<String, List<String>> errorMap = new HashMap<String, List<String>>();
			double score = 0.0;
			
			for (AnnotateImageResponse res : responses) {
				if (res.hasError()) {
					System.out.printf("Error: %s\n", res.getError().getMessage());
					errorMsgList.add(res.getError().getMessage());
				}
				errorMap.put("Errors", errorMsgList);

				for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
					if(annotation.getScore()>score){
						//To check the highest confidence value
						score = annotation.getScore();
						apiResponse.setImageDecodedText(annotation.getDescription());
					}
					
					System.out.println("Description: "+annotation.getDescription());
					annotation.getAllFields().forEach((k, v) -> System.out.printf("%s : %s\n", k, v.toString()));
				}
				apiResponse.setErrorMap(errorMap);
			}
		}
		return apiResponse;
	}

	public static ImageAnnotatorSettings setImageAnnotator() {
		Credentials credentials =null;
		try {
			System.out.println("Inside setImageAnnotator");
			credentials = ComputeEngineCredentials.create();	
			System.out.println("credentials setImageAnnotator");
		}catch (Exception e) {
			e.printStackTrace();
		}
		ImageAnnotatorSettings imageAnnotatorSettings = null;
		try {
			imageAnnotatorSettings = ImageAnnotatorSettings.newBuilder()
					.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
			System.out.println("imageAnnotatorSettings******");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return imageAnnotatorSettings;
	}

	
	public VisionApiResponse getTextForImage(JsonNode node) throws URISyntaxException{
	ResponseEntity<JsonNode> resp;
	VisionApiResponse apiResponse = new VisionApiResponse();
	
	URI serverUrl = new URI("https://vision.googleapis.com/v1/images:annotate?key=AIzaSyBu0GUKiUnrc20TGT2I4WJxV25oqPOYf7g");
	System.out.println("serverUrl: "+serverUrl);
	System.out.println("call service : "+node.toString());
		resp = restTemplate().postForEntity(serverUrl, node, JsonNode.class);
		if(resp.hasBody()){
			JsonNode rootNode = resp.getBody();		
			System.out.println("rootNode"+rootNode);
			//To check the highest confidence value
			JsonNode labelAnnotationList = rootNode.path("labelAnnotations");
			Iterator<JsonNode> labelItr = labelAnnotationList.elements();
			      
			labelItr.forEachRemaining(labelNode -> {
				double score = 0.0;  
				labelItr.next();
				if (labelNode.get("score").asDouble()>score){
					score = labelNode.get("score").asDouble();
					apiResponse.setImageDecodedText(labelNode.get("description").asText());
				}
			});
			System.out.println("apiResponse: "+apiResponse);
		}
		System.out.println("Got response: "+resp.getBody());
		return apiResponse;
	}
	
	@Override
	public String searchByDecodedText(String decodedTest) {
		System.out.println("decodedTest: "+decodedTest);
		String result =null;
		try {				
			searchServiceUrl="http://104.154.92.99/keywordsearch?keyword=";
			System.out.println("searchServiceUrl: "+searchServiceUrl);
				result = restTemplate().getForObject(searchServiceUrl+decodedTest, String.class);	
			System.out.println("response: "+result);
		}catch(HttpClientErrorException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}