package com.projit.storeApp.exception;

public class OrderNotFoundException extends RuntimeException {
	public OrderNotFoundException() {
		super("Order not found");
	}
}
