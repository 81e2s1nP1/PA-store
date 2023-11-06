package com.pa.controller;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pa.model.Account;
import com.pa.model.Brand;
import com.pa.model.Img;
import com.pa.model.Size;
import com.pa.model.User;
import com.pa.repository.Accout_Repository;
import com.pa.repository.UserRepository;
import com.pa.service.AccountDao;
import com.pa.service.BrandDao;
import com.pa.service.ImgDao;
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
	private UserRepository userRepository;

	@Autowired
	private Accout_Repository accout_Repository;

	@Autowired
	private BrandDao brandDao;

	@Autowired
	private SizeDao sizeDao;

	@Autowired
	private ImgDao imgDao;

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
		m.addAttribute("last", new_imgs.size() - 2);
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

//	@PostMapping(path = "/handle-reset-password")
//	public ResponseEntity<Account> handle_reset_password(
//				@RequestBody Account account, HttpSession session, HttpServletRequest request
//			) {
//		}
//		
//		
//	}

	@GetMapping(path = "/detail-product")
	public String detail_product(@RequestParam("id") int id, Model m) {
		List<Size> sizes = sizeDao.findByProduct_id(productDao.findByid(id));
		m.addAttribute("sizes", sizes);

		Brand brand = brandDao.findById(id);
		m.addAttribute("brand", brand);

		List<Img> imgs = imgDao.getAll();
		List<Img> new_imgs = new ArrayList<Img>();

		for (int i = 0; i < imgs.size(); i++) {
			boolean isExist = imgs.get(i).getP().getTen_san_pham().equals(brand.getProduct().getTen_san_pham());
			if (i == 0) {
				if (isExist) {
					new_imgs.add(imgs.get(i));
				} else {
					++i;
				}
			} else {
				if (isExist) {
					new_imgs.add(imgs.get(i));
				}
			}
		}

		List<String> brands = brandDao.getAllBrandName();
		m.addAttribute("brands", brands);
		m.addAttribute("Imgs", new_imgs);
		return "detail-product";
	}

	// xu ly tao tai khoan & thong tin nguoi dung
	@PostMapping(path = "/register-account")
	public String solving_resgister(@Valid @ModelAttribute Account account, @Valid @ModelAttribute User user,
			@RequestParam("dateofbirth") String dob, HttpSession session, HttpServletRequest request) {

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
		if (userRepository.findByUsername(user.getUsername()) == null) {
			userDao.save(user, url);
			accountDao.save(account);
		} else {
			session.setAttribute("msg", "Register fail");
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

	// Xử lý hiển thị các sản phẩm của một thương hiệu
	@GetMapping(path = "/brand")
	public String handle(@RequestParam("brandname") String brandname, Model m) {
		List<String> tableBrands = brandDao.getAllBrandName();
		m.addAttribute("table_brands", tableBrands);

		// detail brand
		List<Img> imgs = imgDao.getAll();
		List<Img> new_imgs = new ArrayList<Img>();

		for (int i = 0; i < imgs.size(); i++) {
			if (i == 0) {
				if (imgs.get(i).getPic().contains(brandname)) {
					new_imgs.add(imgs.get(i));
				} else {
					++i;
				}
			} else {
				if (imgs.get(i).getP().getId() != imgs.get(i - 1).getP().getId()
						&& imgs.get(i).getPic().contains(brandname)) {
					new_imgs.add(imgs.get(i));
				}
			}
		}
		m.addAttribute("products", new_imgs);
		m.addAttribute("name", brandname);
		return "one-brand";
	}

	@PostMapping(path = "/find-product")
	public String find_product(@RequestParam("name") String name, Model m) {
		List<Img> imgs = imgDao.findByProduct_idContain(name);
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
		m.addAttribute("find", name);
		m.addAttribute("products", new_imgs);
		return "find-product";
	}

	@GetMapping(path = "/about")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_USER')")
	public String AboutMe(Principal p, Model m) {
		Account account = accout_Repository.findByEmail(p.getName());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = dateFormat.format(account.getUser().getNgaysinh());
		
		m.addAttribute("dob", formattedDate);
		m.addAttribute("user", account.getUser());
		return "about";
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
