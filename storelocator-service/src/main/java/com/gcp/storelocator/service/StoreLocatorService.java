package com.gcp.storelocator.service;

import java.util.List;

import com.gcp.storelocator.model.StoreDetail;

public interface StoreLocatorService {
	
	public List<StoreDetail> getStoreDetails(String address, String radius);
	
	public List<StoreDetail> getStoresForLatLong(String lat, String lng);
	
	public String getLatLong(String address);	

}
