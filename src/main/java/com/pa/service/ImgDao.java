package com.pa.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudinary.utils.ObjectUtils;
import com.pa.model.Img;
import com.pa.model.Product;
import com.pa.repository.ImgRepository;
import com.cloudinary.Cloudinary;

@Service
public class ImgDao {
	@Autowired
	private ImgRepository imgRepository;
	
	@Autowired
	private Cloudinary cloudinary;
	
	public Img save(Img img) {
		return imgRepository.save(img);
	}
	
	public List<Img> getAll() {
		return imgRepository.getAll();
	}
	
	public List<Img> findByProduct_id(Product p) {
		return imgRepository.getByproduct_id(p);
	}
	
	public List<Img> findByProduct_idContain(String keyword) {
		return imgRepository.findByImgContaining(keyword);
	}
	
	public String uploadImage(File file) {
        try {
            // Sử dụng CloudinaryService để tải lên tệp lên Cloudinary
            Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            // Trích xuất URL của tệp đã tải lên
            String fileUrl = (String) uploadResult.get("url");
            // Trả về URL của tệp đã tải lên
            return fileUrl;
        } catch (IOException e) {
            e.printStackTrace();
            return "Lỗi khi tải lên tệp";
        }
    } 
}
