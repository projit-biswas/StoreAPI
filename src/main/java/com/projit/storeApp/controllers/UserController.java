package com.projit.storeApp.controllers;

import com.projit.storeApp.dtos.RegisterUserRequest;
import com.projit.storeApp.dtos.UpdateUserRequest;
import com.projit.storeApp.dtos.UserDto;
import com.projit.storeApp.entities.Role;
import com.projit.storeApp.mapper.UserMapper;
import com.projit.storeApp.repositories.UserRepository;
import com.projit.storeApp.secvices.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users")
public class UserController {
	private final UserService userService;
	private final UserMapper userMapper;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@GetMapping
	public List<UserDto> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
		var user = userService.getUserById(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(user);
	}

	@PostMapping
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserRequest request, UriComponentsBuilder uriBuilder){
		if (userRepository.existsByEmail(request.getEmail())){
			return ResponseEntity.badRequest().body(
					Map.of("email", "Email is already registered.")
			);
		}
		var user = userMapper.toEntity(request);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(Role.USER);
		userRepository.save(user);

		var userDto = userMapper.toDto(user);
		var uri = uriBuilder.path("/api/v1/users/{id}").buildAndExpand(user.getId()).toUri();
		return ResponseEntity.created(uri).body(userDto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<UserDto> updateUser(@PathVariable(name = "id") Long id , @RequestBody UpdateUserRequest request ){
		var user = userRepository.findById(id).orElse(null);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		userMapper.update(request, user);
		userRepository.save(user);
		return ResponseEntity.ok(userMapper.toDto(user));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable(name = "id") Long id){
		var user = userRepository.findById(id).orElse(null);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		userRepository.delete(user);
		return ResponseEntity.noContent().build();
	}
}
