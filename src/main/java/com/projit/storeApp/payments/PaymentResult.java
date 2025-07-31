package com.projit.storeApp.payments;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaymentResult {
	private Long orderId;
	private PaymentStatus paymentStatus;
}
