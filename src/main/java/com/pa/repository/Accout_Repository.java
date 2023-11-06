	package com.pa.repository;
	
	import org.springframework.data.repository.PagingAndSortingRepository;
	import org.springframework.stereotype.Repository;
	
	import com.pa.model.Account;
	
	@Repository
	public interface Accout_Repository extends PagingAndSortingRepository<Account, Integer> {
		public Account findById(int id);
		public Account findByEmail(String email);
	}
