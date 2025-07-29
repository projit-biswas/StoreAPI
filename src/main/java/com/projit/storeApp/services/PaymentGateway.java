package com.projit.storeApp.services;

import com.projit.storeApp.entities.Order;

public interface PaymentGateway {
	CheckoutSession createCheckoutSession(Order order);
}
