package com.projit.storeApp.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserRequest {
	@NotBlank(message = "Name is required")
	@Size(max = 255, message = "Name must be less than 255 characters")
	String name;

	@NotBlank(message = "Email is required")
	@Email(message = "Email must be valid")
	@Lowercase(message = "Email must be in lowercase.")
	String email;

	@NotBlank(message = "Password is required")
	@Size(min = 6, max = 15, message = "Password must be between 6 to 15 characters long")
	String password;
}
