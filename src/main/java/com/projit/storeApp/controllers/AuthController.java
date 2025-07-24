package com.projit.storeApp.controllers;

import com.projit.storeApp.dtos.JwtResponse;
import com.projit.storeApp.dtos.LoginRequest;
import com.projit.storeApp.dtos.UserDto;
import com.projit.storeApp.mapper.UserMapper;
import com.projit.storeApp.repositories.UserRepository;
import com.projit.storeApp.secvices.JwtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final UserRepository userRepository;
	private final UserMapper userMapper;

	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request){
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getEmail(),
						request.getPassword()
				)
		);

		var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
		var token = jwtService.generateToken(user);
		return ResponseEntity.ok(new JwtResponse(token));
	}

	@PostMapping("/validate")
	public boolean validate(@RequestHeader("Authorization") String authHeader){
		var token = authHeader.replace("Bearer ", "");
		return jwtService.validateToken(token);
	}

	@GetMapping("/current-user")
	public ResponseEntity<UserDto> currentUser() {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		var userId = (Long) authentication.getPrincipal();

		var user = userRepository.findById(userId).orElse(null);
		if (user == null) {
			ResponseEntity.notFound().build();
		}

		var userDto = userMapper.toDto(user);
		return ResponseEntity.ok(userDto);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Void> handleBadCredentials() {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}


}
