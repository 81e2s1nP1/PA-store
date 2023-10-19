package com.pa.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pa.model.User;



@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
	public Boolean findById(int id);
	@Query("SELECT u FROM User u WHERE u.username = :username")
	public User findByUsername(@Param("username") String username);
	public User findByVerification(String verificationCode);

}
