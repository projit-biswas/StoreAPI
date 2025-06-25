package com.projit.storeApp.secvices;

import com.projit.storeApp.mapper.UserMapper;
import com.projit.storeApp.dtos.UserDto;
import com.projit.storeApp.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {
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
}
