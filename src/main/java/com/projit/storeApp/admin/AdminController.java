package com.projit.storeApp.admin;

import com.projit.storeApp.users.RegisterUserRequest;
import com.projit.storeApp.users.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin")
public class AdminController {

	private final UserService userService;

	@GetMapping("/hello")
	public String sayHello() {
		return "Hello Admin...";
	}

	@PostMapping
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserRequest request, UriComponentsBuilder uriBuilder) {
		var userDto = userService.registerUser(request);
		var uri = uriBuilder.path("/api/users/{id}").buildAndExpand(userDto.getId()).toUri();
		return ResponseEntity.created(uri).body(userDto);
	}
}
