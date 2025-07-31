package com.projit.storeApp.payments;

import com.projit.storeApp.orders.Order;

import java.util.Optional;

public interface PaymentGateway {
	CheckoutSession createCheckoutSession(Order order);

	Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);
}
