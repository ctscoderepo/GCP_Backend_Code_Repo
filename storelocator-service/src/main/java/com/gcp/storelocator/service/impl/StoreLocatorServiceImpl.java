package com.gcp.storelocator.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.gcp.storelocator.dao.StoreLocatorDAO;
import com.gcp.storelocator.model.StoreDetail;
import com.gcp.storelocator.service.StoreLocatorService;
import com.gcp.storelocator.util.StoreLocatorConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

@Service
public class StoreLocatorServiceImpl implements StoreLocatorService {

	@Autowired
	StoreLocatorDAO storeLocatorDAO;

	@Value(StoreLocatorConstants.RADIUS)
	private double defaultRadius;

	@Value(StoreLocatorConstants.API_KEY)
	private String api_key;

	private static final Logger logger = LoggerFactory.getLogger(StoreLocatorServiceImpl.class);

	@Override
	public List<StoreDetail> getStoresForLatLong(String lat, String lng) {
		double latitude = Double.parseDouble(lat);
		double longitude = Double.parseDouble(lng);

		return storeLocatorDAO.searchStores(latitude, longitude, defaultRadius);		
	}

	@Override
	public String getLatLong(String address) {

		String response = null;
		final GeocodingResult[] results;
		try {
			results = getGeocodingResult(address);
			if (null != results) {
				final Gson gson = new GsonBuilder().setPrettyPrinting().create();
				System.out.println("gson###########: "+gson.toString());
				if (null != results[0]) {
					response = gson.toJson(results[0].geometry.location);
				}
			}
		} catch (Exception e) {
			logger.error("Error while getting lat/long: ", e.getMessage());
		}
		return response;
	}

	@Override
	public List<StoreDetail> getStoreDetails(String address, String rad) {

		List<StoreDetail> records = new ArrayList<>();
		double radius = defaultRadius;
		if (null != address && !address.isEmpty()) {

			// Get the Geocode for the input address
			final GeocodingResult[] results;
			try {
				results = getGeocodingResult(address);
				System.out.println("results**************: "+results);
				if (null != results && results.length > 0) {
					double lat = results[0].geometry.location.lat;
					double lng = results[0].geometry.location.lng;
					if (null != rad && !rad.isEmpty()) {
						radius = Double.parseDouble(rad);
					}
					records = storeLocatorDAO.searchStores(lat, lng, radius);
				}

			} catch (Exception e) {
				logger.error("Error while getting stores: ", e.getMessage());
			}
		}
		return records;

	}

	private GeocodingResult[] getGeocodingResult(String address) {

		GeocodingResult[] results = null;
		try {
			final GeoApiContext context = new GeoApiContext.Builder().apiKey(api_key).build();
			results = GeocodingApi.geocode(context, address).await();
			System.out.println("results: "+results);
		} catch (Exception e) {
			logger.error("Error while getting geocode from Google : ", e.getMessage());
		}
		return results;
	}

}
