package com.projit.storeApp.services;

import com.projit.storeApp.dtos.CheckoutRequest;
import com.projit.storeApp.dtos.CheckoutResponse;
import com.projit.storeApp.entities.Order;
import com.projit.storeApp.exception.CartEmptyException;
import com.projit.storeApp.exception.CartNotFoundException;
import com.projit.storeApp.repositories.CartRepository;
import com.projit.storeApp.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CheckoutService {
	private CartRepository cartRepository;
	private OrderRepository orderRepository;
	private AuthService authService;
	private CartService cartService;

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
		cartService.clearCart(cart.getId());
		return new CheckoutResponse(order.getId());
	}
}
