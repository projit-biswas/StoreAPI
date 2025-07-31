package com.projit.storeApp.users;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;

	public List<UserDto> getAllUsers() {
		return userRepository.findAll()
				.stream()
				.map(userMapper::toDto)
				.toList();
	}

	public UserDto getUserById(Long id) {
		var user = userRepository.findById(id).orElse(null);
		if (user != null) {
			return new UserDto(user.getId(), user.getName(), user.getEmail());
		}
		return null;
	}

	public UserDto registerUser(RegisterUserRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new EmailAlreadyExistsException("Email is already exists");
		}
		var user = userMapper.toEntity(request);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(Role.USER);
		userRepository.save(user);

		return userMapper.toDto(user);
	}

	public UserDto updateUser(Long id, UpdateUserRequest request) {
		var user = userRepository.findById(id).orElse(null);
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		userMapper.update(request, user);
		userRepository.save(user);
		return userMapper.toDto(user);
	}

	public void deleteUser(Long id) {
		var user = userRepository.findById(id).orElse(null);
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		userRepository.delete(user);
	}
}
