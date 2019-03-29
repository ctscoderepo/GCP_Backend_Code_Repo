package com.gcp.storelocator.dao.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import com.gcp.storelocator.dao.StoreLocatorDAO;
import com.gcp.storelocator.model.StoreDetail;
import com.gcp.storelocator.util.StoreLocatorConstants;

@Repository
public class StoreLocatorDaoImpl implements StoreLocatorDAO {

	@PersistenceContext
	private EntityManager entityManager;

	protected Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	@Value(StoreLocatorConstants.SEARCH_STORES_TIMEOUT)
	private int searchCarryoutStoresTimeout;

	@Value(StoreLocatorConstants.SEARCH_RESULTS_LIMIT)
	private int searchResultsLimit;

	private static final Logger logger = LoggerFactory.getLogger(StoreLocatorDaoImpl.class);

	@SuppressWarnings("all")
	@Override
	public List<StoreDetail> searchStores(double lat, double lng, double radius) {
		double latitude = lat;
		double longitude = lng;

		String DBQuery = "SELECT id, address, store_info, lat, lon, "
				+ "( 3959 * acos ( cos ( radians(:latitude) ) * cos( radians( lat ) ) * cos( radians( lon ) - radians(:longitude) ) + sin ( radians(:latitude) ) * sin( radians( lat ) ) ) ) AS distance "
				+ "FROM stores HAVING distance < :radius ORDER BY distance LIMIT :searchResultsLimit ";
		logger.info("Query for getting stores", DBQuery);
		StringBuilder query = new StringBuilder(DBQuery);

		return getCurrentSession().createSQLQuery(query.toString()).addEntity(StoreDetail.class)
				.setDouble("longitude", longitude).setDouble("latitude", latitude).setDouble("radius", radius)
				.setInteger("searchResultsLimit", searchResultsLimit).setTimeout(searchCarryoutStoresTimeout).list();
	}
}
