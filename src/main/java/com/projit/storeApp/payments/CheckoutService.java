package com.projit.storeApp.payments;

import com.projit.storeApp.auth.AuthService;
import com.projit.storeApp.carts.CartEmptyException;
import com.projit.storeApp.carts.CartNotFoundException;
import com.projit.storeApp.carts.CartRepository;
import com.projit.storeApp.carts.CartService;
import com.projit.storeApp.orders.Order;
import com.projit.storeApp.orders.OrderRepository;
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

	public void handleWebhook(WebhookRequest request) {
		paymentGateway.parseWebhookRequest(request).ifPresent((paymentResult) -> {
			var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
			order.setStatus(paymentResult.getPaymentStatus());
			orderRepository.save(order);
		});
	}
}
