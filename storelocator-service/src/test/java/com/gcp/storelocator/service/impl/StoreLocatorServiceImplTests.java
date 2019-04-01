package com.gcp.storelocator.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import com.gcp.storelocator.dao.StoreLocatorDAO;
import com.gcp.storelocator.model.StoreDetail;

@RunWith(SpringRunner.class)
public class StoreLocatorServiceImplTests {

	@Mock
	StoreLocatorDAO storeLocatorDAO;
	
	@Test
	public void getStoresForLatLong(){
		double mockLat= 38.2580403;
		double mockLng= -85.6427234;
		double defaultRadius = 5;
		List<StoreDetail> records = mockResponse();
		when(storeLocatorDAO.searchStores(mockLat, mockLng, defaultRadius)).thenReturn(records);		
		assertThat(records).hasSize(1);
	}
	public List<StoreDetail> mockResponse(){
		StoreDetail mockResponse = new StoreDetail();
		mockResponse.setID("1");
		mockResponse.setLat(38.2580403);
		mockResponse.setLng(-85.6427234);
		mockResponse.setAddress("4174 Westport Rd, Louisville, KY 40207");
		mockResponse.setStore_info("8:00 am - 11:00 pm");
		List<StoreDetail> records = new ArrayList<>();	
		records.add(mockResponse);
		return records;
	}
}
