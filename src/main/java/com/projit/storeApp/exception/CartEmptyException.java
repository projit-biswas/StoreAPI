package com.projit.storeApp.exception;

public class CartEmptyException extends RuntimeException {
	public CartEmptyException() {
		super("Cart is empty");
	}
}
