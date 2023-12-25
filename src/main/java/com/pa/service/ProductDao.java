package com.pa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.pa.model.Brand;
import com.pa.model.Img;
import com.pa.model.Orders;
import com.pa.model.Product;
import com.pa.model.Size;
import com.pa.repository.Brand_Repository;
import com.pa.repository.ImgRepository;
import com.pa.repository.OrdersRepository;
import com.pa.repository.Product_Repository;
import com.pa.repository.SizeRepository;

@Service
public class ProductDao {
	@Autowired
	private Product_Repository product_Repository;
	
	@Autowired
	private ImgRepository imgRepository;
	
	@Autowired
	private SizeRepository sizeRepository;
	
	@Autowired
	private Brand_Repository brand_Repository;
	
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
	
	public Product update_product(Product product, int id) {
		Product p = product_Repository.findById(id);
		if(p != null){
			p.setId(id);
			p.setTen_san_pham(product.getTen_san_pham());
			p.setSoluong(product.getSoluong());
			p.setSale(product.getSale());
			p.setMota(product.getMota());
			p.setGiaban(product.getGiaban());
			return product_Repository.save(p);
		}else {			
			return null;
		}
	}
	
	public List<Product> findByLikeTen_san_pham(String keyword) {
		return product_Repository.findByTitleContaining(keyword);
	}
	
	public boolean Delete(int id) {
		boolean isDelete = false; 
		Product product  = product_Repository.findById(id);
		if(product != null) {
			List<Img> imgs = imgRepository.getByproduct_id(product);
			List<Size> sizes = sizeRepository.getByproduct_id(product);
			Brand brand = brand_Repository.getByproduct_id(product);
 			for(Img img: imgs) {
				imgRepository.delete(img);
			}			
 			for(Size size: sizes) {
 				sizeRepository.delete(size);
 			}
 			brand_Repository.delete(brand);
			product_Repository.delete(product);
			isDelete = true;
		}
		return isDelete;
	}
	
	public List<Product> getProductByPage(int page, int pageSize) {
        return product_Repository.getProductByPage(page, pageSize);
    }
	
	public List<Product> getRandom8Product(){
		Pageable pageable = PageRequest.of(0, 8);
	    return product_Repository.getRandom8Product(pageable);
	}
}
