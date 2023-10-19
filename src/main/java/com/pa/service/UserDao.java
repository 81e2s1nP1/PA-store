package com.pa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.pa.model.User;
import com.pa.repository.UserRepository;

import java.util.UUID;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

@Service
public class UserDao {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JavaMailSender mailSender;
	
	
	public User save(User user, String url) {
			user.setEnable(false);
			user.setVerification(UUID.randomUUID().toString());
			User u = userRepository.save(user);
			if(u != null) {
				sendEmail(user, url);
			}
			return u;
	}
	
	public void removeSessionMessage() {
	    ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	    
	    if (requestAttributes != null) {
	       HttpSession session = requestAttributes.getRequest().getSession(false);
	        
	        if (session != null && session.getAttribute("msg") != null) {
	            session.removeAttribute("msg");
	        }
	    }
		
	}

	public void sendEmail(User user, String url) {
		String from = "tranphuocan301003@gmail.com";
		String to = user.getAccount().getEmail();
		String subject = "Account Verfication";
		String content = "Dear [[name]],<br>" + "Please click the link below to verify your registration:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you,<br>" + "PAIT";

		try {

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);

			helper.setFrom(from, "Mr.PA");
			helper.setTo(to);
			helper.setSubject(subject);

			content = content.replace("[[name]]", user.getUsername());
			String siteUrl = url + "/verify?code=" + user.getVerification();

			System.out.println(siteUrl);

			content = content.replace("[[URL]]", siteUrl);

			helper.setText(content, true);

			mailSender.send(message);

		} catch (Exception e) {
			e.printStackTrace();
		}	
		
	}

	public boolean verifyAccount(String verificationCode) {
		System.out.println("code" + verificationCode);
		User user = userRepository.findByVerification(verificationCode);
		System.out.println("user" + user);
		if (user == null) {
			return false;
		} else {
			user.setEnable(true);
			System.out.println(user.isEnable());
			user.setVerification(null);
			System.out.println(user.getVerification());
			System.out.println(user);
			userRepository.save(user);
			return true;
			}
		}
}
