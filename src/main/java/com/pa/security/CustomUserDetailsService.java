package com.pa.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.pa.model.Account;
import com.pa.repository.Accout_Repository;

	@Component
	public class CustomUserDetailsService implements UserDetailsService {
		@Autowired
		private Accout_Repository accout_Repository;
		
		@SuppressWarnings("unused")
		@Override
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			Account a = accout_Repository.findByEmail(username);
			if(a == null ) {
				throw new UsernameNotFoundException("not found username");
			}else {
				return new CustomUserDetails(a);
			}
		}
	
	}
