package com.pa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pa.model.Orders;
import com.pa.model.User;

@Repository
public interface OrdersRepository extends PagingAndSortingRepository<Orders, java.lang.Integer> {
	@Query("SELECT o FROM Orders o WHERE o.user = :user")
	public List<Orders> getMyPurchase(@Param("user") User user);
}
