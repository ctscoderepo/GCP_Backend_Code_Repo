package com.gcp.cart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.gcp.cart.domain.Orders;

/**
 * @author Anuj Kumar
 * 
 * This class represent repository for orders table
 */
@Repository
public interface OrdersRepository extends CrudRepository<Orders, Integer>{

	/**
	 * This method is find pending order w.r.t. of a customer
	 * 
	 * @param Orders
	 * @return List<OrderItems>
	 */
	@Query("from Orders o where o.memberId = :memberId and o.status = :status")
	Orders findPendingOrderByMemberId(@Param("memberId") long memberId, @Param("status") String status);
	
	/**
	 * This method is find all list of orders w.r.t. of a customer
	 * 
	 * @param Orders
	 * @return List<OrderItems>
	 */
	@Query("from Orders o where o.memberId = :memberId")
	List<Orders> findOrderByMemberId(@Param("memberId") long memberId);
	
	/**
	 * This method is find order w.r.t. orderId
	 * 
	 * @param orderItemId
	 * @return OrderItems
	 */
	@Query("from Orders o where o.id = :id")
	Orders findOrderByOrdersId(@Param("id") int id);
	
}