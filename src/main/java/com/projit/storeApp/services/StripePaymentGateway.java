package com.projit.storeApp.services;

import com.projit.storeApp.entities.Order;
import com.projit.storeApp.entities.OrderItem;
import com.projit.storeApp.exception.PaymentException;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class StripePaymentGateway implements PaymentGateway {

	@Value("${websiteUrl}")
	private String websiteUrl;

	@Override
	public CheckoutSession createCheckoutSession(Order order) {
		try {
			var sessionBuilder = SessionCreateParams.builder()
					.setMode(SessionCreateParams.Mode.PAYMENT)
					.setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
					.setCancelUrl(websiteUrl + "/checkout-cancel");

			order.getItems().forEach(item -> {
				var lineItem = createLineItem(item);
				sessionBuilder.addLineItem(lineItem);
			});

			var session = Session.create(sessionBuilder.build());
			return new CheckoutSession(session.getUrl());

		} catch (StripeException ex) {
			System.out.println(ex.getMessage());
			throw new PaymentException();
		}
	}

	private SessionCreateParams.LineItem createLineItem(OrderItem item) {
		return SessionCreateParams.LineItem.builder()
				.setQuantity(Long.valueOf(item.getQuantity()))
				.setPriceData(
						createPriceData(item)
				).build();
	}

	private SessionCreateParams.LineItem.PriceData createPriceData(OrderItem item) {
		return SessionCreateParams.LineItem.PriceData.builder()
				.setCurrency("inr")
				.setUnitAmountDecimal(item.getUnitPrice().multiply(BigDecimal.valueOf(100)))
				.setProductData(createProductData(item))
				.build();
	}

	private SessionCreateParams.LineItem.PriceData.ProductData createProductData(OrderItem item) {
		return SessionCreateParams.LineItem.PriceData.ProductData.builder()
				.setName(item.getProduct().getName())
				.setDescription(item.getProduct().getDescription())
				.build();
	}
}
