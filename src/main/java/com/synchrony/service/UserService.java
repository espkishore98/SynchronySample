package com.synchrony.service;

import org.springframework.http.ResponseEntity;

import com.synchrony.entity.User;

public interface UserService {

	ResponseEntity RegisterUser(User user);

}
