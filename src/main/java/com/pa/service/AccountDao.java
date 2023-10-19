package com.pa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.pa.model.Account;
import com.pa.repository.Accout_Repository;

@Service
public class AccountDao {
	@Autowired
	private Accout_Repository accout_repository; 
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public Account save(Account acc) {
			acc.setPassword(passwordEncoder.encode(acc.getPassword()));
			Account a = accout_repository.save(acc);
			return a;
	}
}
