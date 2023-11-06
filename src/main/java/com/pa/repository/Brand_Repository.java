package com.pa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pa.model.Brand;
import com.pa.model.Product;

@Repository
public interface Brand_Repository extends PagingAndSortingRepository<Brand, Integer> {
	@Query("SELECT DISTINCT  b.brand_name FROM Brand b")
	public List<String> getAllBrandName();
	
	@Query("SELECT b FROM Brand b WHERE b.brand_name = :brand_name")
	public List<Brand> getByBrandname(@Param("brand_name") String brand_name);
	
	public Brand findById(int id);
	
	@Query("SELECT b FROM Brand b WHERE b.product = :product")
	public Brand getByproduct_id(@Param("product") Product product);
}
