package com.projit.storeApp.carts;

public class CartNotFoundException extends RuntimeException {
	public CartNotFoundException() {
		super("Cart not found");
	}
}
