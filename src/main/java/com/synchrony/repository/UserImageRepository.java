package com.synchrony.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.synchrony.entity.UserImages;

public interface UserImageRepository extends JpaRepository<UserImages, Long> {
	
	@Query(value="select i from UserImages i where i.userId = :id")
	List<UserImages> getByUserId(Long id);

}
