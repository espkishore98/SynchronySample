package com.synchrony.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.synchrony.entity.User;

@Repository
public interface UserRepository extends JpaRepository<com.synchrony.entity.User, Long> {
	
	@Query(value="select u from User where u.userName = :userName", nativeQuery=false)
	public User getUserByUserName(String userName);
}
