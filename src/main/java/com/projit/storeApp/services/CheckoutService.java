package com.projit.storeApp.services;

import com.projit.storeApp.dtos.CheckoutRequest;
import com.projit.storeApp.dtos.CheckoutResponse;
import com.projit.storeApp.entities.Order;
import com.projit.storeApp.exception.CartEmptyException;
import com.projit.storeApp.exception.CartNotFoundException;
import com.projit.storeApp.exception.PaymentException;
import com.projit.storeApp.repositories.CartRepository;
import com.projit.storeApp.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class CheckoutService {
	private final CartRepository cartRepository;
	private final OrderRepository orderRepository;
	private final AuthService authService;
	private final CartService cartService;
	private final PaymentGateway paymentGateway;

	@Transactional
	public CheckoutResponse checkout(CheckoutRequest request) {
		var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
		if (cart == null) {
			throw new CartNotFoundException();
		}
		if (cart.isEmpty()) {
			throw new CartEmptyException();
		}

		var order = Order.createOrder(cart, authService.getCurrentUser());
		orderRepository.save(order);


		try {
			var session = paymentGateway.createCheckoutSession(order);
			cartService.clearCart(cart.getId());
			return new CheckoutResponse(order.getId(), session.getCheckoutUrl());
		} catch (PaymentException ex) {
			orderRepository.delete(order);
			throw ex;
		}
	}
}
