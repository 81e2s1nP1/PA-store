package com.pa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pa.model.Product;
import com.pa.repository.Product_Repository;

@Service
public class ProductDao {
	@Autowired
	private Product_Repository product_Repository;
	
	public List<Product> getAll() {
		return (List<Product>) product_Repository.findAll();
	}
	
	public Product save(Product product) {
		return product_Repository.save(product);
	}
	
	public Product findByid(int id) {
		return product_Repository.findById(id);
	}
	
	public Product findByTenSanPham(String tenSanPham) {
		return product_Repository.getByTenSanPham(tenSanPham);
	}
	
	public List<Product> findByListTenSanPham(String tenSanPham) {
		return product_Repository.getByListTenSanPham(tenSanPham);
	}
	
	public Boolean update_sale(int persent_sale) {
		Boolean isSale = false;
		if(persent_sale > 1 && persent_sale <= 10) {
			isSale = product_Repository.updateSale(persent_sale);
		}
		return isSale;
	}
	
	public List<Product> findByLikeTen_san_pham(String keyword) {
		return product_Repository.findByTitleContaining(keyword);
	}
}
