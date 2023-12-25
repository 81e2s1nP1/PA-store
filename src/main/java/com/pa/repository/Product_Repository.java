package com.pa.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pa.model.Product;


@Repository
public interface Product_Repository extends PagingAndSortingRepository<Product, Integer> {
	public Product findById(int id);
	
	@Query("SELECT p FROM Product p WHERE p.ten_san_pham = :ten_san_pham")
	public Product  getByTenSanPham(@Param("ten_san_pham") String ten_san_pham);
	
	@Query("SELECT p FROM Product p WHERE p.ten_san_pham = :ten_san_pham")
	public List<Product> getByListTenSanPham(@Param("ten_san_pham") String ten_san_pham);
	
	@Query("UPDATE Product p SET p.sale = p.giaban * ( :sale /100 )")
	public Boolean updateSale(@Param("sale") int sale);
	
	@Query("SELECT p FROM Product p WHERE p.ten_san_pham LIKE CONCAT('%', :keyword, '%')")
	List<Product> findByTitleContaining(@Param("keyword") String keyword);
	
	@Query(value = "SELECT * FROM Product LIMIT :pageSize OFFSET :offset", nativeQuery = true)
	List<Product> getProductByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);
	
	@Query("SELECT p FROM Product p")
	List<Product> get4ProductFirst();
	
	@Query("SELECT p FROM Product p ORDER BY RAND()")
    List<Product> getRandom8Product(Pageable pageable);
}
