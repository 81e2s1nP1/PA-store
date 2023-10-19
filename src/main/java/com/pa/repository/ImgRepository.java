package com.pa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pa.model.Img;
import com.pa.model.Product;

@Repository
public interface ImgRepository extends PagingAndSortingRepository<Img, Integer> {
	@Query("SELECT DISTINCT i FROM Img i")
	public List<Img> getAll(); 
	
	@Query("SELECT i FROM Img i WHERE i.product = :product")
	public List<Img> getByproduct_id(@Param("product") Product product);
	
	@Query("SELECT DISTINCT i FROM Img i WHERE i.product.ten_san_pham LIKE CONCAT('%', :keyword, '%')")
	List<Img> findByImgContaining(@Param("keyword") String keyword);
}
