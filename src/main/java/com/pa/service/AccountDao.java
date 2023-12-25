package com.pa.service;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.pa.model.Account;
import com.pa.model.User;
import com.pa.repository.Accout_Repository;

@Service
public class AccountDao {
	@Autowired
	private Accout_Repository accout_repository; 
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JavaMailSender mailSender;
	
	public Account save(Account acc) {
			acc.setPassword(passwordEncoder.encode(acc.getPassword()));
			Account a = accout_repository.save(acc);
			return a;
	}
	
	public void sendEmail(Account account, String url) {
		String from = "tranphuocan070197@gmail.com";
		String to = account.getEmail();
		String subject = "Account Verfication";
		String content = "Dear [[name]],<br>" + "Please click the link below to reset your password:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">RESET PASSWORD</a></h3>" + "Thank you,<br>" + "PAIT";

		try {

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);

			helper.setFrom(from, "Mr.PA");
			helper.setTo(to);
			helper.setSubject(subject);

			content = content.replace("[[name]]", account.getUser().getUsername());
			String siteUrl = url + "/reset-password?email="+account.getEmail();

			content = content.replace("[[URL]]", siteUrl);

			helper.setText(content, true);

			mailSender.send(message);

		} catch (Exception e) {
			e.printStackTrace();
		}	
		
	}
}
