package com.projit.storeApp.auth;

import com.projit.storeApp.users.User;
import com.projit.storeApp.users.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
	private final UserRepository userRepository;

	public User getCurrentUser() {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		var userId = (Long) authentication.getPrincipal();
		return userRepository.findById(userId).orElse(null);
	}
}
