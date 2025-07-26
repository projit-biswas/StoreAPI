package com.projit.storeApp.exception;

public class CartNotFoundException extends RuntimeException {
	public CartNotFoundException() {
		super("Cart not found");
	}
}
