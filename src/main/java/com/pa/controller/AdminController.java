package com.pa.controller;

import java.io.File;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pa.model.Account;
import com.pa.model.Brand;
import com.pa.model.Img;
import com.pa.model.Orders;
import com.pa.model.Product;
import com.pa.model.Size;
import com.pa.repository.Accout_Repository;
import com.pa.repository.ImgRepository;
import com.pa.service.BrandDao;
import com.pa.service.ImgDao;
import com.pa.service.OrdersDao;
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
	private OrdersDao ordersDao;
	
	@Autowired
	private Accout_Repository accout_Repository;
	
	@Autowired
	private ImgRepository imgRepository;
	
	@ModelAttribute
	public void commonUser(Principal p, Model m) {
		if (p != null) {
			String email = p.getName();
			Account account = accout_Repository.findByEmail(email);
			m.addAttribute("user", account);
		}
	}
	
	@GetMapping(path = "/save-product")
	public String save_product(Model m){
		List<String> brands = brandDao.getAllBrandName();
		m.addAttribute("brands", brands);
		return "fill-infor-prdct";
	}

	
	@SuppressWarnings("unused")
	@PostMapping(path = "/handle-save-product")
	public String handle_save_product(@Valid @ModelAttribute Product product,
	       @RequestParam("brandname") String brandname,
	       @Valid @ModelAttribute Size size,
	       RedirectAttributes redirectAttributes
			){
		Product p = productDao.findByTenSanPham(product.getTen_san_pham());
		List<Size> sizes = null;
		Product result = null;
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
				result = productDao.save(p);
		 }else {
			sizes = new ArrayList<Size>();
			sizes.add(size);
			size.setSoLuongSize(size.getSoLuongSize());
			size.setProduct(product);
			product.setSoluong(product.getSoluong() + size.getSoLuongSize());
			
			String url = "D:\\SpringBoot\\Website-banhang-backup\\src\\main\\resources\\static\\img\\product\\"+brandname+"\\"+product.getTen_san_pham();
			File directory = new File(url);
			File[] files = directory.listFiles();
			
			List<String> images = new ArrayList<String>();
			if(files != null) {				
				String relativeDirectory = "";
				for (File file : files) {
					if (file.isFile() && file.getName().endsWith(".jpg")) {
						String path = imgDao.uploadImage(file);
						images.add(path);
					}else if(file == null) {
						redirectAttributes.addFlashAttribute("error", "The product does not exist in stock");
						return "redirect:save-product";
					}
				}
			}else {
				redirectAttributes.addFlashAttribute("error", "The product does not exist in stock");
			    return "redirect:save-product";
			}
			//end list files
			result = productDao.save(product);
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
		if(result != null) {
			redirectAttributes.addFlashAttribute("message", "The product has been saved successfully.");
		}else {
			redirectAttributes.addFlashAttribute("error", "Product saved failed");
			return "redirect:/admin/update-product";
		}
	    return "redirect:save-product";
	}

	
	@GetMapping(path =  "/products")
	public String getAll(Model m,  @RequestParam(defaultValue = "0") int page) {
		if(page < 0 ) {
			page = 0;
			return "redirect:/admin/products?page="+page;
		}
		int pageSize = 5;
        List<Product> products = productDao.getProductByPage(page, pageSize);
        if(products.isEmpty()) {
        	page -= 5;
        	return "redirect:/admin/products?page="+page;
        }
        List<String> brands = brandDao.getAllBrandName();
		m.addAttribute("brands", brands);
        m.addAttribute("products", products);
        m.addAttribute("currentPage", page);
		return "products";
	}
	
	@GetMapping(path = "/about-product")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String AboutMe(@RequestParam("id") int id, Model m) {
		Product product = productDao.findByid(id);
		if(product != null) {	
			List<String> brands = brandDao.getAllBrandName();
			m.addAttribute("brands", brands);
			m.addAttribute("product", product);
			return "about-product";
		}else {
			return "redirect:/error";
		}
	}
	
	@PostMapping(path = "/update-product")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String UpdateAbout(@ModelAttribute Product product,
            RedirectAttributes redirectAttributes
			) {
		Product p = productDao.update_product(product, product.getId());
		if(p != null) {
			redirectAttributes.addFlashAttribute("message", "The product has been updated successfully.");
		}else {
			redirectAttributes.addFlashAttribute("error", "Product update failed");
			return "redirect:/admin/update-product";
		}
		return "redirect:/admin/products";
	}
	
	@GetMapping(path = "/delete-product")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String Delete_Product(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
		boolean isDelete = productDao.Delete(id);
		if(isDelete) {
			redirectAttributes.addFlashAttribute("message", "The product has been Deleted successfully.");
		}else {
			redirectAttributes.addFlashAttribute("error", "Product delete failed");
		}
		return "redirect:/admin/products";
	}
	
	@GetMapping(path = "/customer-orders")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String ordered(Principal p, Model m,  @RequestParam(defaultValue = "0") int page, RedirectAttributes redirectAttributes) {
		int pageSize = 5;

	    if (page < 0) {
	        page = 0;
	    }

	    List<Orders> orders = ordersDao.getOrdersByPageAdmin(page, pageSize);

	    if (orders.isEmpty() && page > 0) {
	        page = Math.max(0, page - 5);
	        return "redirect:/admin/customer-orders?page=" + page;
	    } else if (orders.isEmpty()) {
	        redirectAttributes.addFlashAttribute("message", "chưa có sản phẩm nào");
	        return "AllPurchase";
	    }
	    // Top 4 Best Seller
	    List<Product> top8BestSeller = productDao.getRandom8Product();
	    List<Img> imgTop8BestSeller = top8BestSeller.stream()
	            .map(product -> imgRepository.getByproduct_id(product).get(0))
	            .collect(Collectors.toList());
	    
	    for(Orders order: orders) {
	    	order.setImg(imgRepository.getByproduct_id(order.getProduct()).get(0));
	    }
	    // Other Attributes
	    m.addAttribute("top4BestSeller", imgTop8BestSeller);

	    List<String> brands = brandDao.getAllBrandName();
	    m.addAttribute("brands", brands);
	    m.addAttribute("orders", orders);
	    m.addAttribute("currentPage", page);

	    return "AllPurchase";
		}
	
	@GetMapping(path = "/deleteOrder")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String deleteOrder(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
		boolean isDelete = ordersDao.deleteOrder(id);
		if(isDelete) {
			redirectAttributes.addFlashAttribute("message", "The Order has been Deleted successfully.");
		}else {
			redirectAttributes.addFlashAttribute("message", "Order delete failed");
		}			
		return "redirect:/admin/customer-orders";
	}
}
