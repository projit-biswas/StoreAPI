package com.projit.storeApp.auth;

import com.projit.storeApp.users.UserDto;
import com.projit.storeApp.users.UserMapper;
import com.projit.storeApp.users.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
	private final JwtConfig jwtConfig;
	private final UserRepository userRepository;
	private final UserMapper userMapper;

	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getEmail(),
						request.getPassword()
				)
		);

		var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
		var accessToken = jwtService.generateAccessToken(user);
		var refreshToken = jwtService.generateRefreshToken(user);

		var cookie = new Cookie("refreshToken", refreshToken.toString());
		cookie.setHttpOnly(true);
		cookie.setPath("/api/auth/refresh");
		cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
		cookie.setSecure(true);
		response.addCookie(cookie);

		return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
	}

	@PostMapping("/refresh")
	public ResponseEntity<JwtResponse> refresh(@CookieValue("refreshToken") String refreshToken) {
		var jwt = jwtService.parseToken(refreshToken);
		if (jwt == null || jwt.isExpired()) {
			ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		var user = userRepository.findById(jwt.getUserId()).orElseThrow();
		var accessToken = jwtService.generateAccessToken(user);
		return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
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
