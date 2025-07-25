package com.projit.storeApp.services;

import com.projit.storeApp.dtos.UserDto;
import com.projit.storeApp.mapper.UserMapper;
import com.projit.storeApp.repositories.UserRepository;
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
import java.util.List;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {
	private final UserRepository userRepository;
	private final UserMapper userMapper;

	public List<UserDto> getAllUsers() {
		return userRepository.findAll()
				.stream()
				.map(userMapper::toDto)
				.toList();
	}

	public UserDto getUserById(Long id) {
		var user = userRepository.findById(id).orElse(null);
		if (user != null) {
			return new UserDto(user.getId(),user.getName(),user.getEmail());
		}
		return null;
	}

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
