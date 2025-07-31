package com.projit.storeApp.users;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users")
public class UserController {
	private final UserService userService;

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
		var userDto = userService.registerUser(request);
		var uri = uriBuilder.path("/api/v1/users/{id}").buildAndExpand(userDto.getId()).toUri();
		return ResponseEntity.created(uri).body(userDto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<UserDto> updateUser(@PathVariable(name = "id") Long id , @RequestBody UpdateUserRequest request ){
		return ResponseEntity.ok(userService.updateUser(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") Long id) {
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}
}
