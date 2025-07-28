package com.projit.storeApp.services;

import com.projit.storeApp.dtos.CheckoutRequest;
import com.projit.storeApp.dtos.CheckoutResponse;
import com.projit.storeApp.entities.Order;
import com.projit.storeApp.exception.CartEmptyException;
import com.projit.storeApp.exception.CartNotFoundException;
import com.projit.storeApp.repositories.CartRepository;
import com.projit.storeApp.repositories.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CheckoutService {
	private final CartRepository cartRepository;
	private final OrderRepository orderRepository;
	private final AuthService authService;
	private final CartService cartService;

	@Value("${websiteUrl}")
	private String websiteUrl;

	public CheckoutResponse checkout(CheckoutRequest request) throws StripeException {
		var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
		if (cart == null) {
			throw new CartNotFoundException();
		}
		if (cart.isEmpty()) {
			throw new CartEmptyException();
		}

		var order = Order.createOrder(cart, authService.getCurrentUser());
		orderRepository.save(order);

		// create a checkout session
		var builder = SessionCreateParams.builder()
				.setMode(SessionCreateParams.Mode.PAYMENT)
				.setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
				.setCancelUrl(websiteUrl + "/checkout-cancel");

		order.getItems().forEach(item -> {
			var lineItem = SessionCreateParams.LineItem.builder()
					.setQuantity(Long.valueOf(item.getQuantity()))
					.setPriceData(
							SessionCreateParams.LineItem.PriceData.builder()
									.setCurrency("usd")
									.setUnitAmountDecimal(item.getUnitPrice())
									.setProductData(
											SessionCreateParams.LineItem.PriceData.ProductData.builder()
													.setName(item.getProduct().getName())
													.setDescription(item.getProduct().getDescription())
													.build()
									).build()
					).build();
			builder.addLineItem(lineItem);
		});

		var session = Session.create(builder.build());

		cartService.clearCart(cart.getId());
		return new CheckoutResponse(order.getId(), session.getUrl());
	}
}
