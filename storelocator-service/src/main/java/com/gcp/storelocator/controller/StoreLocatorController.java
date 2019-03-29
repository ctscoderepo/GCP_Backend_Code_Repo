package com.gcp.storelocator.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.gcp.storelocator.model.StoreDetail;
import com.gcp.storelocator.service.StoreLocatorService;

@CrossOrigin
@RestController
public class StoreLocatorController {
	
	@Autowired
	StoreLocatorService storeLocatorService;
	
	private static final Logger logger = LoggerFactory.getLogger(StoreLocatorController.class);
	
	@GetMapping(value = "/getstoresforlatlong", produces = MediaType.APPLICATION_JSON_VALUE)
	 public ResponseEntity<List<StoreDetail>> getStoresForLatLong(@RequestParam("lat") String lat, @RequestParam("lng") String lng, Model model) {
		 
		 List<StoreDetail> storeRecords = storeLocatorService.getStoresForLatLong(lat, lng);
		 logger.info("Store Records: "+storeRecords);
		 model.addAttribute("storeData", storeRecords);	
		return new ResponseEntity<>(storeRecords, HttpStatus.OK);
	 }
	
	@GetMapping(value = "/getgeocode", produces = MediaType.APPLICATION_JSON_VALUE)
	 public ResponseEntity<String> getGeoCode(@RequestParam("address") String address, Model model) {
		 
		 String geoCode = storeLocatorService.getLatLong(address);
		 logger.info("GeoCode Info: "+geoCode);
		 model.addAttribute("geocode", geoCode);	
		return new ResponseEntity<String>(geoCode, HttpStatus.OK);
	 }
	
	@GetMapping(value = "/getstoredetails", produces = MediaType.APPLICATION_JSON_VALUE)
	 public ResponseEntity<List<StoreDetail>> getStoreDetails(@RequestParam("address") String address, @RequestParam("radius") String radius, Model model) {
		 
		 List<StoreDetail> storeRecords = storeLocatorService.getStoreDetails(address,radius);
		 logger.info("Store Records: "+storeRecords);
		 model.addAttribute("storeData", storeRecords);	
		return new ResponseEntity<>(storeRecords, HttpStatus.OK);
	 }

}
