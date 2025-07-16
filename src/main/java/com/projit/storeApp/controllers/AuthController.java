package com.projit.storeApp.controllers;

import com.projit.storeApp.dtos.JwtResponse;
import com.projit.storeApp.dtos.LoginRequest;
import com.projit.storeApp.secvices.JwtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication")
public class AuthController {

	private AuthenticationManager authenticationManager;
	private JwtService jwtService;

	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request){
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getEmail(),
						request.getPassword()
				)
		);
		var token = jwtService.generateToken(request.getEmail());
		return ResponseEntity.ok(new JwtResponse(token));
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Void> handleBadCredentials() {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@PostMapping("/validate")
	public boolean validate(@RequestHeader("Authorization") String authHeader){
		var token = authHeader.replace("Bearer ", "");
		return jwtService.validateToken(token);
	}
}
