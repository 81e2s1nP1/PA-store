package com.pa.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
				HttpSession session = request.getSession(true);
		
		        // Thêm một thuộc tính vào session khi đăng nhập thất bại
		        session.setAttribute("notify_login", "Email or Password wrong !");
		
		        // Redirect đến URL sau khi đăng nhập thất bại
		        response.sendRedirect("/login");
	}

}
