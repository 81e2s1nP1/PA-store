package com.pa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pa.model.Orders;
import com.pa.model.Product;
import com.pa.model.User;

@Repository
public interface OrdersRepository extends PagingAndSortingRepository<Orders, java.lang.Integer> {
	@Query("SELECT o FROM Orders o WHERE o.user = :user")
	public List<Orders> getMyPurchase(@Param("user") User user);
	
	@Query("SELECT o FROM Orders o WHERE o.product = :product")
	public List<Orders> getByproduct_id(@Param("product") Product product);
	
	@Query("SELECT o.product, COUNT(o) AS orderCount"
			+ " FROM Orders o"
			+ " GROUP BY o.product"
			+ " ORDER BY orderCount DESC")
	List<Product> findTop4ProductsByCount();
	
	@Query(value = "SELECT * FROM orders o WHERE o.user_id = :userId LIMIT :pageSize OFFSET :offset", nativeQuery = true)
	List<Orders> getOrdersByPage(@Param("offset") int offset, @Param("pageSize") int pageSize, @Param("userId") int userId);
	
	@Query(value = "SELECT * FROM Orders LIMIT :pageSize OFFSET :offset", nativeQuery = true)
	List<Orders> getOrdersByPageAdmin(@Param("offset") int offset, @Param("pageSize") int pageSize);
	
	List<Orders> findAll();
}
