package com.projit.storeApp.carts;

public class CartEmptyException extends RuntimeException {
	public CartEmptyException() {
		super("Cart is empty");
	}
}
