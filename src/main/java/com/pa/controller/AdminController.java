package com.pa.controller;

import java.io.File;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pa.model.Account;
import com.pa.model.Brand;
import com.pa.model.Img;
import com.pa.model.Product;
import com.pa.model.Size;
import com.pa.repository.Accout_Repository;
import com.pa.service.BrandDao;
import com.pa.service.ImgDao;
import com.pa.service.ProductDao;
import com.pa.service.SizeDao;

@Controller
@RequestMapping(path = "/admin")
public class AdminController {
	@Autowired
	private ProductDao productDao; 
	
	@Autowired
	private BrandDao brandDao;
	
	@Autowired 
	private SizeDao sizeDao;
	
	@Autowired 
	private ImgDao imgDao;
	
	@Autowired
	private Accout_Repository accout_Repository;
	
	@ModelAttribute
	public void commonUser(Principal p, Model m) {
		if (p != null) {
			String email = p.getName();
			Account account = accout_Repository.findByEmail(email);
			m.addAttribute("user", account);
		}
	}
	
	@GetMapping(path = "/save-product")
	public String save_product(){
		return "fill-infor-prdct";
	}

	
	@SuppressWarnings("unused")
	@PostMapping(path = "/handle-save-product")
	public String handle_save_product(@Valid @ModelAttribute Product product,
	       @RequestParam("brandname") String brandname,
	       @Valid @ModelAttribute Size size
			){
		Product p = productDao.findByTenSanPham(product.getTen_san_pham());
		List<Size> sizes = null;
		//nếu sản phẩm đã tồn tại trong database
		if(p != null) {
			List<Size> Obj_sizes = sizeDao.findBySize_ao(size.getSize_ao());
			Size ObjSize = null;
			for(Size s: Obj_sizes) {
				if(s.getProduct().getId() == p.getId()) {
					ObjSize = s;
				}
			} 
			// nếu size này của sản phẩm đã có trong database
			if(ObjSize != null) {
				ObjSize.setSoLuongSize(ObjSize.getSoLuongSize() + size.getSoLuongSize());
					p.setSoluong(p.getSoluong() + size.getSoLuongSize());
			 	}else {
			 		p.setSoluong(p.getSoluong() + size.getSoLuongSize());
					size.setProduct(p);
					sizeDao.save(size);
			 	}
					productDao.save(p);
		 }else {
			sizes = new ArrayList<Size>();
			sizes.add(size);
			size.setSoLuongSize(size.getSoLuongSize());
			size.setProduct(product);
			product.setSoluong(product.getSoluong() + size.getSoLuongSize());
			// lưu đối tượng Product 
			// list files 
			String url = "D:\\SpringBoot\\Website-banhang-backup\\src\\main\\resources\\static\\img\\product\\"+brandname+"\\"+product.getTen_san_pham();
			File directory = new File(url);
			File[] files = directory.listFiles();

			// Tạo mảng để lưu ảnh
			List<String> images = new ArrayList<String>();
			String relativeDirectory = "";
			// Lặp qua tất cả các tệp
			for (File file : files) {
				System.out.println("file: " + file);
			    // Kiểm tra xem tệp có phải là ảnh hay không
			    if (file.isFile() && file.getName().endsWith(".jpg")) {
			    	String str_file = file.toString();
			    	relativeDirectory = ""+str_file.substring(str_file.indexOf("\\img"));
			        // Thêm đường dẫn của ảnh vào mảng
			    	images.add(relativeDirectory);
			    }
			}
			//end list files
			productDao.save(product);
		    sizeDao.save(size);
			List<Img> imgs = new ArrayList<>();
			for(String image: images) {
				Img Obj_img = new Img(image, product);
				imgDao.save(Obj_img);
			}
		    
		    Brand brand = new Brand();
		    brand.setBrand_name(brandname);
		    brand.setProduct(product);
		    
		    brandDao.save(brand); // Lưu Brand sau khi lưu Product
		}
	    return "redirect:save-product";
	}

	
	@GetMapping("/getAll")
	public ResponseEntity<List<Product>> getAll() {
		return new ResponseEntity<List<Product>>(productDao.getAll(), HttpStatus.OK);
	}
}
