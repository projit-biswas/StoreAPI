package com.projit.storeApp.orders;

public class OrderNotFoundException extends RuntimeException {
	public OrderNotFoundException() {
		super("Order not found");
	}
}
