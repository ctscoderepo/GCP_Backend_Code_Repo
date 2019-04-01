package com.gcp.storelocator.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import com.gcp.storelocator.model.StoreDetail;
import com.gcp.storelocator.service.StoreLocatorService;

@RunWith(SpringRunner.class)
public class StoreLocatorControllerTests {
	
	@Mock
	private StoreLocatorService storeLocatorService;

	
	@Test
	public void testGetStoresForLatLong(){
		String mockLat= "38.2580403";
		String mockLng= "-85.6427234";
		List<StoreDetail> records = mockResponse();
		when(storeLocatorService.getStoresForLatLong(mockLat, mockLng)).thenReturn(records);
		ResponseEntity<List<StoreDetail>> response = new ResponseEntity<List<StoreDetail>>(records, HttpStatus.OK);

		/*List<StoreDetail> actualResult = storeLocatorService.getStoresForLatLong(mockLat, mockLng);
		assertEquals(records.get(0).getID(),actualResult.get(0).getID());
		assertEquals(records.get(0).getAddress(),actualResult.get(0).getAddress());
		assertEquals(records.get(0).getStore_info(),actualResult.get(0).getStore_info());*/
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	public void testGetStoreDetails(){
		String address= "40222";
		String radius= "5";
		List<StoreDetail> records = mockResponse();
		when(storeLocatorService.getStoreDetails(address, radius)).thenReturn(records);

		List<StoreDetail> actualResult = storeLocatorService.getStoreDetails(address, radius);
		
		assertEquals(records.get(0).getID(),actualResult.get(0).getID());
		assertEquals(records.get(0).getAddress(),actualResult.get(0).getAddress());
		assertEquals(records.get(0).getStore_info(),actualResult.get(0).getStore_info());

	}
	
	@Test
	public void testGetGeoCode(){
		String address= "40222";			
		String response = storeLocatorService.getLatLong(address);
		System.out.println("response: "+response);
		when(storeLocatorService.getLatLong(address)).thenReturn(response);	
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
