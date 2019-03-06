package com.gcp.vision.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.gcp.vision.api.client.ImageSearchClient;
import com.gcp.vision.api.model.FinalResponse;
import com.gcp.vision.api.service.ImageSearchService;

@RestController
public class ImageSearchController {
	
	@Autowired
	protected ImageSearchService imageSearchService;
	
	@Autowired
	protected ImageSearchClient imageSearchClient;
	
/*	@PostMapping(path = "/searchImage")
	public ResponseEntity<?> getProductsList(@RequestBody String imageUrl) throws Exception{
		FinalResponse response = imageSearchService.decodeImage(imageUrl);	
		System.out.println("response: "+response);
		return new ResponseEntity<FinalResponse>(response, HttpStatus.OK);
	}*/
	
	@PostMapping(path = "/searchImage")
	public ResponseEntity<?> getProductsList(@RequestBody JsonNode node) throws Exception{
		ResponseEntity<?> response = imageSearchClient.getTextForImage(node);	
		System.out.println("response: "+response);
		return response;
	}
}