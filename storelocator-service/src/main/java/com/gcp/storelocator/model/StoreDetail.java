package com.gcp.storelocator.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gcp.storelocator.util.StoreLocatorConstants;

@Entity
@Table(name = StoreLocatorConstants.TABLE_NAME_STORES)
public class StoreDetail {
	
	@Id
	private String ID;
	
	@Column(name = "LAT")
	private double lat;
	
	@Column(name = "LON")
	private double lng;
	
	@Column(name = "ADDRESS")
	private String address;
	
	@Column(name = "STORE_INFO")
	private String store_info;
	
	public String getID() {
		return ID;
	}
	public void setID(String id) {
		this.ID = id;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getStore_info() {
		return store_info;
	}
	public void setStore_info(String store_info) {
		this.store_info = store_info;
	}
}
