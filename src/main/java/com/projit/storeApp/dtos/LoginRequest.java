package com.projit.storeApp.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
	@NotBlank(message = "Email is required")
	@Email
	String email;

	@NotBlank(message = "Password is required")
	@Size(min = 6, max = 15, message = "Password must be between 6 to 15 characters long")
	String password;
}
