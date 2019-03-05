package com.gcp.storelocator.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gcp.storelocator.model.StoreDetail;

@Repository
public interface StoreLocatorDAO {
	
	List<StoreDetail> searchStores(double lat, double lng, double radius);

}
