package com.synchrony.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.synchrony.entity.User;

@Repository
public interface UserRepository extends JpaRepository<com.synchrony.entity.User, Long> {
	
	@Query(value="select u from User u where u.userName = :userName", nativeQuery=false)
	User getByuserName(String userName);
	@Query(value="select u from User u where u.externalId = :externalId", nativeQuery=false)
	User findByExernalId(String externalId);
}
