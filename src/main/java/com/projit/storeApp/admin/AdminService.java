package com.projit.storeApp.admin;

import com.projit.storeApp.users.*;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AdminService {
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;

	public UserDto registerUser(RegisterUserRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new EmailAlreadyExistsException("Email already exists");
		}
		var user = userMapper.toEntity(request);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(Role.ADMIN);
		userRepository.save(user);

		return userMapper.toDto(user);
	}
}
