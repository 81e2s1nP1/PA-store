package com.pa.controller;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.pa.model.Orders;
import com.pa.model.Product;
import com.pa.model.Size;
import com.pa.repository.Accout_Repository;
import com.pa.service.BrandDao;
import com.pa.service.ImgDao;
import com.pa.service.OrdersDao;
import com.pa.service.ProductDao;
import com.pa.service.SizeDao;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private Accout_Repository accout_Repository;

	@Autowired
	private SizeDao sizeDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private ImgDao imgDao;

	@Autowired
	private BrandDao brandDao;

	@Autowired
	private OrdersDao ordersDao;

	@ModelAttribute
	public void commonUser(Principal p, Model m) {
		if (p != null) {
			String email = p.getName();
			Account account = accout_Repository.findByEmail(email);
			m.addAttribute("user", account);
		}
	}

	@PostMapping(path = "/buy")
	public String handle_buy(@RequestParam("product_id") int id, Model m) {
		Product p = productDao.findByid(id);
		Brand brand = brandDao.findByProduct_id(p);
		// lấy ra hình ảnh đầu tiên của sản phẩm
		List<Img> imgs = imgDao.findByProduct_id(p);
		Img img = imgs.get(0);
		List<Size> sizes = sizeDao.findByProduct_id(p);
		if (img.getP().getSale() != 0) {
			m.addAttribute("total", img.getP().getGiaban() - img.getP().getSale());
		} else {
			m.addAttribute("total", img.getP().getGiaban());
		}
		m.addAttribute("brand", brand);
		m.addAttribute("sizes", sizes);
		m.addAttribute("img", img);
		return "create-order";
	}

	@PostMapping(path = "/buyed")
	public String buyed(@ModelAttribute Orders orders, Principal p, @RequestParam("id") int id, Model m) {
		//thay thế tất cả dấu , có trong chỗi size thành rỗng
		orders.setSize(orders.getSize().replaceAll(",", ""));
		Product product = productDao.findByid(id); 
		Size size = sizeDao.findByProductAndSize_ao(orders.getSize(), product);
		//nếu size và product khác yêu cầu có trong csdl
		if (product != null && size != null) {
			if (size.getSoLuongSize() <= 0) {
				m.addAttribute("msg", "Size bạn chọn tạm hết vui lòng chọn size khác");
				return "redirect:/detail-product?id="+id;
			} else {
				String email = p.getName();
				Account account = accout_Repository.findByEmail(email);
				orders.setProduct(product);
				orders.setTong_tien(product.getGiaban() - product.getSale());
				orders.setUser(account.getUser());
				orders.setNgaydat(new Date());

				// lưu đơn hàng
				ordersDao.save(orders);
				product.setSoluong(product.getSoluong() - 1);
				size.setSoLuongSize(size.getSoLuongSize() - 1);
				sizeDao.save(size);
				productDao.save(product);
			}
		} else {
			return "redirect:/error";
		}
		return "redirect:/detail-product?id="+id;
	}
	
	@GetMapping(path = "/purchase")
	public String ordered(Principal p, Model m) {
		if(p == null) {
			return "redirect:/login";
		}else {
			Account account = accout_Repository.findByEmail(p.getName());
			m.addAttribute("orders", ordersDao.getByMyPurchase(account.getUser()));
			System.out.println(ordersDao.getByMyPurchase(account.getUser()));
			return "MyPurchase";
			}
		}
}
