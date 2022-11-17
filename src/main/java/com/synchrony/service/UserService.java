package com.synchrony.service;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import com.synchrony.domain.LoginRequest;
import com.synchrony.entity.User;
import com.synchrony.repository.UserImageRepository;

@Service
public interface UserService {

	ResponseEntity registerUser(User user);
//	User fetchUserDetailsById(Long userId);
//	User fetchUserDetailsByUserName(String userName);

	ResponseEntity<?> getUserById(Long userId);
	ResponseEntity upload(MultipartFile file,String bearerToken);

	ResponseEntity loginUser(LoginRequest request);

	ResponseEntity findAllImgsByUserId(String bearerToken);

//	ResponseEntity findByImageByKey(String imageKey, String authToken);

	ResponseEntity findByImageByKey(String imageKey);



}
