package com.gcp.cart.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.gcp.cart.domain.Order;

@Repository
public interface CartRepository extends CrudRepository<Order, Long>{

}
