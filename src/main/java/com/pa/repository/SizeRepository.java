package com.pa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pa.model.Product;
import com.pa.model.Size;

@Repository
public interface SizeRepository extends PagingAndSortingRepository<Size, Integer> {
	@Query("SELECT s FROM Size s WHERE s.size_ao = :size_ao")
	public List<Size> getBySize(@Param("size_ao") String size_ao);
	
	@Query("SELECT s FROM Size s WHERE s.product = :product")
	public List<Size> getByproduct_id(@Param("product") Product product);

	    @Query("SELECT s FROM Size s WHERE s.size_ao = :size_ao AND s.product = :product")
	    Size findBySizeAoAndProduct(@Param("size_ao") String sizeAo, @Param("product") Product product);
}
