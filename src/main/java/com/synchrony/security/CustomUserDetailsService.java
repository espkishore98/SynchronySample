package com.synchrony.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synchrony.entity.User;
import com.synchrony.repository.UserRepository;



@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String externalId) {

		User user = userRepository.getByuserName(externalId);

		return UserPrincipal.create(user);
	}

	public UserDetails loadUserByExternalId(String externalId) {
		// TODO Auto-generated method stub
		User user = userRepository.findByExernalId(externalId);
		return UserPrincipal.create(user);
	}

}
