package com.projit.storeApp.services;

import com.projit.storeApp.entities.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaymentResult {
	private Long orderId;
	private PaymentStatus paymentStatus;
}
