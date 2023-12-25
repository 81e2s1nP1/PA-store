package com.pa.controller;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pa.model.Account;
import com.pa.model.Brand;
import com.pa.model.Img;
import com.pa.model.Orders;
import com.pa.model.Product;
import com.pa.model.Size;
import com.pa.model.User;
import com.pa.repository.Accout_Repository;
import com.pa.repository.ImgRepository;
import com.pa.repository.OrdersRepository;
import com.pa.repository.SizeRepository;
import com.pa.repository.UserRepository;
import com.pa.service.AccountDao;
import com.pa.service.BrandDao;
import com.pa.service.ImgDao;
import com.pa.service.OrdersDao;
import com.pa.service.ProductDao;
import com.pa.service.SizeDao;
import com.pa.service.UserDao;

@Controller
public class HomeController {
	@Autowired
	private UserDao userDao;

	@Autowired
	private AccountDao accountDao;

	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private OrdersDao ordersDao;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private Accout_Repository accout_Repository;

	@Autowired
	private OrdersRepository ordersRepository;
	
	@Autowired
	private SizeRepository sizeRepository;
	
	@Autowired
	private BrandDao brandDao;

	@Autowired
	private SizeDao sizeDao;

	@Autowired
	private ImgDao imgDao;
	
	@Autowired
	private ImgRepository imgRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@GetMapping(path = "/")
	public String home(Model m, @RequestParam("p") Optional<Integer> p) {
		List<Img> imgs = imgDao.getAll();
		List<Img> new_imgs = new ArrayList<Img>();
		for (int i = 0; i < imgs.size(); i++) {
			if (i == 0) {
				new_imgs.add(imgs.get(i));
			} else {
				if (imgs.get(i).getP().getId() != imgs.get(i - 1).getP().getId()) {
					new_imgs.add(imgs.get(i));
				}
			}
		}
		p = Optional.ofNullable(p.orElse(0));
		List<Img> subarray = PagingCustom(new_imgs, p, p.flatMap(i -> Optional.of(i + 9)));
		m.addAttribute("products", subarray);
		m.addAttribute("current", p.get());
		m.addAttribute("last", new_imgs.size() - 9);
		List<String> brands = brandDao.getAllBrandName();
		m.addAttribute("brands", brands);
		return "index";
	}

	private static final List<Img> PagingCustom(List<Img> new_imgs, Optional<Integer> p, Optional<Integer> optional) {
		List<Img> subArray = new ArrayList<>();
		int limit = Math.min(optional.get(), new_imgs.size());
		for (int i = p.get(); i < limit; i++) {
			subArray.add(new_imgs.get(i));
		}
		return subArray;
	}

	@ModelAttribute
	public void commonUser(Principal p, Model m) {
		if (p != null) {
			String email = p.getName();
			Account account = accout_Repository.findByEmail(email);
			m.addAttribute("user", account);
		}
	}

	@GetMapping(path = "/login")
	public String login() {
		return "login";
	}

	@GetMapping(path = "/user-logout")
	public String logout() {
		return "redirect:/";
	}

	@GetMapping(path = "/create")
	public String cretate() {
		return "create";
	}

	// xu ly dat lai password
	@GetMapping(path = "/reset-password")
	public String reset_password() {
		return "reset-password";
	}
	
	@GetMapping(path = "/reset-password-email")
	public String reset_password_email() {
		return "YourEmail";
	}
	
	@PostMapping(path = "/handle-email") 
	public String sendEmail(@RequestParam("email") String email,
			RedirectAttributes redirectAttributes, 
			HttpServletRequest request) {
		Account account = accout_Repository.findByEmail(email);
		if(account == null) {
			redirectAttributes.addFlashAttribute("error", "E-mail does not exist");
			return "redirect:/reset-password-email";
		}
		String url = request.getRequestURL().toString();
		url = url.replace(request.getServletPath(), "");
		accountDao.sendEmail(account, url);
		return "redirect:/login";
	}

	@PostMapping(path = "/handle-reset-password")
	public String ResetPassword(@RequestParam("password") String password,
			@RequestParam("email") String email,
			@RequestParam("confirm_password") String confirm_password,
			RedirectAttributes redirectAttributes) {
		Account account = accout_Repository.findByEmail(email);
		if(account == null) {
			redirectAttributes.addFlashAttribute("error", "E-mail does not exist");
			return "redirect:/reset-password?email="+email;
		}
		if(!password.equals(confirm_password)) {
			redirectAttributes.addFlashAttribute("error", "Password Incorrect");
			return "redirect:/reset-password?email="+email;
		}
		account.setPassword(passwordEncoder.encode(confirm_password));
		accout_Repository.save(account);
		return "redirect:/login";
	}

	@GetMapping(path = "/detail-product")
	public String detailProduct(@RequestParam("id") int id, Model m) {
	    // Sizes
	    List<Size> sizes = sizeDao.findByProduct_id(productDao.findByid(id));
	    m.addAttribute("sizes", sizes);

	    // Brand
	    Brand brand = brandDao.findById(id);
	    m.addAttribute("brand", brand);

	    // Product Images
	    List<Img> imgs = imgDao.getAll();
	    List<Img> newImgs = new ArrayList<>();

	    String targetProductName = brand.getProduct().getTen_san_pham();

	    for (int i = 0; i < imgs.size(); i++) {
	        boolean isExist = imgs.get(i).getP().getTen_san_pham().equals(targetProductName);

	        if ((i == 0 && isExist) || (i > 0 && isExist)) {
	            newImgs.add(imgs.get(i));
	        }
	    }

	    // Top 4 Best Seller
	    List<Product> top4BestSeller = ordersDao.findTop4Ordered();
	    List<Img> imgTop4BestSeller = top4BestSeller.stream()
	            .map(product -> imgRepository.getByproduct_id(product).get(0))
	            .collect(Collectors.toList());

	    // Other Attributes
	    List<String> brands = brandDao.getAllBrandName();
	    m.addAttribute("top4BestSeller", imgTop4BestSeller);
	    m.addAttribute("brands", brands);
	    m.addAttribute("Imgs", newImgs);

	    return "detail-product";
	}


	// xu ly tao tai khoan & thong tin nguoi dung
	@PostMapping(path = "/register-account")
	public String solving_resgister(@Valid @ModelAttribute Account account, @Valid @ModelAttribute User user,
			@RequestParam("dateofbirth") String dob, RedirectAttributes redirectAttributes, HttpServletRequest request) {

		String url = request.getRequestURL().toString();
		// System.out.println(url); http://localhost:8080/saveUser
		url = url.replace(request.getServletPath(), "");

		// convert String to date
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		Date date = null;
		try {
			date = (Date) formatter.parse(dob);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		account.setRoles("ROLE_USER");
		user.setNgaysinh(date);
		account.setUser(user);
		user.setAccount(account);

		// save user & account into db
		if (userRepository.findByUsername(user.getUsername()) == null
				&& accout_Repository.findByEmail(account.getEmail()) == null
				) {
			userDao.save(user, url);
			accountDao.save(account);
		} else {
			redirectAttributes.addFlashAttribute("msg", "Username Or Email Existed");
		}
		return "redirect:create";
	}

	@GetMapping(path = "/verify")
	public String verify(@Param("code") String code, Model m) {
		boolean f = userDao.verifyAccount(code);
		if (f)
			m.addAttribute("msg", "veverify success");
		else
			m.addAttribute("msg", "may be your vefication code is incorrect or already veified");
		return "verified";
	}

	@GetMapping(path = "/brand")
	public String handleBrand(
	        @RequestParam("brandname") String brandname,
	        Model m,
	        @RequestParam("p") Optional<Integer> p
	) {
	    List<String> tableBrands = brandDao.getAllBrandName();
	    m.addAttribute("table_brands", tableBrands);

	    // detail brand
	    List<Img> imgs = imgDao.getAll();
	    List<Img> new_imgs = new ArrayList<>();

	    for (int i = 0; i < imgs.size(); i++) {
	        String orginalStr = imgs.get(i).getP().getTen_san_pham().toLowerCase();
	        String subStrToCheck = brandname.toLowerCase();

	        boolean isFirstImg = i == 0;
	        boolean isNewBrand = (isFirstImg || (imgs.get(i).getP().getId() != imgs.get(i - 1).getP().getId()));
	        boolean isBrandMatched = orginalStr.contains(subStrToCheck);

	        if ((isFirstImg && isBrandMatched) || (isNewBrand && isBrandMatched)) {
	            new_imgs.add(imgs.get(i));
	        }
	    }

	    p = Optional.ofNullable(p.orElse(0));
	    List<Img> subarray = PagingCustom(new_imgs, p, p.flatMap(i -> Optional.of(i + 9)));

	    m.addAttribute("current", p.get());
	    m.addAttribute("last", Math.max(0, new_imgs.size() - 9));

	    List<String> brands = brandDao.getAllBrandName();
	    m.addAttribute("brands", brands);
	    m.addAttribute("products", subarray);
	    m.addAttribute("name", brandname);
	    return "one-brand";
	}


	@GetMapping(path = "/find-product")
	public String find_product(
	        @RequestParam("name") String name,
	        Model m,
	        @RequestParam("p") Optional<Integer> p
	) {
	    List<Img> imgs = imgDao.findByProduct_idContain(name);
	    List<Img> new_imgs = new ArrayList<>();

	    for (int i = 0; i < imgs.size(); i++) {
	        if (i == 0 || imgs.get(i).getP().getId() != imgs.get(i - 1).getP().getId()) {
	            new_imgs.add(imgs.get(i));
	        }
	    }

	    p = Optional.ofNullable(p.orElse(0));
	    List<Img> subarray = PagingCustom(new_imgs, p, p.flatMap(i -> Optional.of(i + 9)));

	    m.addAttribute("current", p.get());
	    m.addAttribute("last", Math.max(0, new_imgs.size() - 9));
	    List<String> brands = brandDao.getAllBrandName();
	    m.addAttribute("brands", brands);
	    m.addAttribute("find", name);
	    m.addAttribute("products", subarray);

	    return "find-product";
	}


	@GetMapping(path = "/about")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_USER')")
	public String AboutMe(Principal p, Model m) {
		Account account = accout_Repository.findByEmail(p.getName());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = dateFormat.format(account.getUser().getNgaysinh());
		
		m.addAttribute("dob", formattedDate);
		m.addAttribute("about", account.getUser());
		List<String> brands = brandDao.getAllBrandName();
		m.addAttribute("brands", brands);
		return "About";
	}
	
	@PostMapping(path = "/update-about")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_USER')")
	public String UpdateAbout(@ModelAttribute User user, @RequestParam("username") String username,
            @RequestParam("address") String address,
            @RequestParam("phone") String phone,
            @RequestParam("dateofbirth") String dob,
            @RequestParam("sex") String sex,
            RedirectAttributes redirectAttributes
			) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		Date date = null;
		try {
			date = (Date) formatter.parse(dob);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		user.setNgaysinh(date);
		user.setAddress(address);
		user.setPhone(phone);
		user.setUsername(username);
		user.setSex(sex);
		User u = userRepository.save(user);
		if(u != null) {
			redirectAttributes.addFlashAttribute("message", "The user has been updated successfully.");
		}else {
			redirectAttributes.addFlashAttribute("message", "User update failed");
		}
		return "redirect:/about";
	}
	
	@GetMapping(path = "/edit-order")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_USER')")
	public String  EditOrder(@RequestParam("id") int id, Model m) {
		Orders orders = ordersRepository.findById(id).get();
		List<Size> sizes = sizeRepository.getByproduct_id(orders.getProduct());
		List<String> brands = brandDao.getAllBrandName();
		m.addAttribute("brands", brands);
		m.addAttribute("order", orders);
		m.addAttribute("sizes", sizes);
		return "about-order";
	}
	
	@PostMapping(path = "/handleEditOrder")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_USER')")
	public String handleEditOrder(@ModelAttribute Orders order, @RequestParam("size") String size, RedirectAttributes redirectAttributes) {
		Orders orders = ordersDao.updateOrder(order, order.getId());
		if(orders != null) {
			redirectAttributes.addFlashAttribute("message", "The user has been updated successfully.");
			return "redirect:/edit-order?id="+ order.getId() ;
		}else {
			redirectAttributes.addFlashAttribute("message", "Order update failed");
			return "redirect:/edit-order?id="+ order.getId() ;
		}
	}
	
	@GetMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if(status != null && Integer.valueOf(status.toString()) == HttpStatus.NOT_FOUND.value())  {
        	return "404";
        }
		return "error"; 
    }
}
