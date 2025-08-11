package com.projit.storeApp.auth;

import com.projit.storeApp.users.UserMapper;
import com.projit.storeApp.users.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;
	private final UserMapper userMapper;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		var user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found."));
		return new User(
				user.getEmail(),
				user.getPassword(),
				Collections.emptyList()
		);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Void> badCredential(){
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
}
