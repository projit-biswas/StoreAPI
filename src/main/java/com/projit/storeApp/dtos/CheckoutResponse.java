package com.projit.storeApp.dtos;

import lombok.Data;

@Data
public class CheckoutResponse {
	private Long orderId;
	private String checkoutUrl;

	public CheckoutResponse(Long orderId, String checkoutUrl) {
		this.orderId = orderId;
		this.checkoutUrl = checkoutUrl;
	}
}
