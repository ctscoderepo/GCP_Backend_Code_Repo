package com.gcp.storelocator.util;

public class StoreLocatorConstants {

	public static final String  TABLE_NAME_STORES="stores";
	
	public static final String DBQuery = "SELECT id, address, store_info, lat, lon, "
			+ "( 3959 * acos ( cos ( radians(38.2351533) ) * cos( radians( lat ) ) * cos( radians( lon ) - radians(-85.5723084) ) + sin ( radians(38.2351533) ) * sin( radians( lat ) ) ) ) AS distance "
			+ "FROM stores HAVING distance < 5 ORDER BY distance LIMIT 5;";
	
	public static final String RADIUS="${store.radius}";

	public static final String SEARCH_STORES_TIMEOUT = "${store.searchStores.timeout}";

	public static final String API_KEY = "${google.api.key}";

	public static final String SWAGGER_BASE_PACKAGE = "${swagger.basePackage}";
	
	public static final String SWAGGER_INFO = "${swagger.info}";
	
	public static final String SWAGGER_PACKAGE = "${swagger.package}";

	public static final String SEARCH_RESULTS_LIMIT = "${store.searchResultsLimit}";
	
}
