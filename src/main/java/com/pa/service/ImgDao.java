package com.pa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pa.model.Img;
import com.pa.model.Product;
import com.pa.repository.ImgRepository;

@Service
public class ImgDao {
	@Autowired
	private ImgRepository imgRepository;
	
	public Img save(Img img) {
		return imgRepository.save(img);
	}
	
	public List<Img> getAll() {
		return imgRepository.getAll();
	}
	
//	public List<Img> getAll(Pageable pageable) {
//		return imgRepository.getAll(pageable);
//	}
	
	public List<Img> findByProduct_id(Product p) {
		return imgRepository.getByproduct_id(p);
	}
	
	public List<Img> findByProduct_idContain(String keyword) {
		return imgRepository.findByImgContaining(keyword);
	}
}
