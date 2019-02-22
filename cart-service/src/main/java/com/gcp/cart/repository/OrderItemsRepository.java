package com.gcp.cart.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.gcp.cart.domain.OrderItems;
import com.gcp.cart.domain.Orders;

/**
 * @author Anuj Kumar
 * 
 * This class represent repository for orderitems table
 */
@Repository
public interface OrderItemsRepository extends CrudRepository<OrderItems, Integer>{
	
	/**
	 * This method is find all the order items w.r.t. orderId
	 * 
	 * @param Orders
	 * @return List<OrderItems>
	 */
	@Query("from OrderItems o where o.orders = :orders")
	List<OrderItems> findOrderItemsByOrdersId(@Param("orders") Orders order);
	
	/**
	 * This method is find order item w.r.t. orderItemId
	 * 
	 * @param orderItemId
	 * @return OrderItems
	 */
	@Query("from OrderItems o where o.id = :id")
	OrderItems findOrderItemsId(@Param("id") Integer orderItemId);
}
