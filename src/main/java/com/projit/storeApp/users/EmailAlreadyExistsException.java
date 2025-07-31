package com.projit.storeApp.users;

public class EmailAlreadyExistsException extends RuntimeException {
	public EmailAlreadyExistsException(String message) {
		super(message);
	}
}
