package com.projit.storeApp.services;

import com.projit.storeApp.entities.Order;

import java.util.Optional;

public interface PaymentGateway {
	CheckoutSession createCheckoutSession(Order order);

	Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);
}
