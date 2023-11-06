package com.pa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pa.model.Brand;
import com.pa.model.Product;
import com.pa.repository.Brand_Repository;

@Service
public class BrandDao {
	@Autowired
	private Brand_Repository brand_Repository;
	
	public List<Brand> getAll() {
		return  (List<Brand>) brand_Repository.findAll();
	}
	
	public List<String> getAllBrandName() {
		return brand_Repository.getAllBrandName();
	}
	
	public List<Brand> findByBrandName(String brandName) {
		return brand_Repository.getByBrandname(brandName);
	}
	
	public Brand save(Brand brand) {
		return brand_Repository.save(brand);
	}
	
	public Brand findById(int id) {
		return brand_Repository.findById(id);
	}
	
	public Brand findByProduct_id(Product p) {
		return  brand_Repository.getByproduct_id(p);
	}
}
