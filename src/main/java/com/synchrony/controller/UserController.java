package com.synchrony.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.synchrony.domain.LoginRequest;
import com.synchrony.entity.User;
import com.synchrony.repository.UserImageRepository;
import com.synchrony.service.UserService;

@RestController
public class UserController {

	@Autowired
	UserService userService;
	@Autowired
	UserImageRepository userImageRepo;
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		return userService.registerUser(user);
	}

	@GetMapping("/getUser/{userId}")
	public ResponseEntity<?> registerUser(@PathVariable Long userId) {
		return userService.getUserById(userId);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		return userService.loginUser(loginRequest);
	}

	@PostMapping("/uploadImg")
	public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file, @RequestHeader("Authorization") String bearerToken ) {
		return userService.upload(file,bearerToken);

	}
	@GetMapping("/getAllImagedtls")
	public ResponseEntity findAllUploadedImages(@RequestHeader("Authorization") String bearerToken) {
		return userService.findAllImgsByUserId(bearerToken);
	}
	
	@GetMapping("/getImageByKey/{imgKey}")
	public ResponseEntity findImageByKey(@PathVariable String imgKey) {
		return userService.findByImageByKey(imgKey);
	}
}
