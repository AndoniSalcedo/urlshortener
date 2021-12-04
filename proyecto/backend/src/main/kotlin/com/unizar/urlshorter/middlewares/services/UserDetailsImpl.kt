/* package com.unizar.urlshorter.middlewares.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unizar.urlshorter.repositories.UserRepository
import com.unizar.urlshorter.models.User

@Service
class UserDetailsServiceImpl : UserDetailsService {
	@Autowired
	var userRepository: UserRepository;

	
	@Transactional
	override fun loadUserByUsername(id: String) : UserDetails {
		var user = userRepository.findById(id)

		return UserDetailsImpl.build(user);
	}

} */