package com.pa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pa.model.Product;
import com.pa.model.Size;
import com.pa.repository.SizeRepository;

@Service
public class SizeDao {
	@Autowired
	private SizeRepository sizeRepository;
	
	public Size save(Size size) {
		return sizeRepository.save(size);
	}
	
	public List<Size> findBySize_ao(String size_ao) {
		return sizeRepository.getBySize(size_ao);
	}
	
	public List<Size> findByProduct_id(Product p) {
		return sizeRepository.getByproduct_id(p);
	}
	
	public Size findByProductAndSize_ao(String s, Product p) {
		return sizeRepository.findBySizeAoAndProduct(s, p);
	}
}
