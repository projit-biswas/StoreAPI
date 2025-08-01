package com.projit.storeApp.payments;

import com.projit.storeApp.orders.Order;
import com.projit.storeApp.orders.OrderItem;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StripePaymentGateway implements PaymentGateway {

	@Value("${websiteUrl}")
	private String websiteUrl;

	@Value("${stripe.webhookSecretKey}")
	private String webhookSecretKey;

	@Override
	public CheckoutSession createCheckoutSession(Order order) {
		try {
			var sessionBuilder = SessionCreateParams.builder()
					.setMode(SessionCreateParams.Mode.PAYMENT)
					.setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
					.setCancelUrl(websiteUrl + "/checkout-cancel")
					.putMetadata("order_id", order.getId().toString());

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

	@Override
	public Optional<PaymentResult> parseWebhookRequest(WebhookRequest request) {
		try {
			var payload = request.getPayload();
			var signature = request.getHeaders().get("stripe-signature");
			var event = Webhook.constructEvent(payload, signature, webhookSecretKey);
			System.out.println(event.getType());

			return switch (event.getType()) {
				case "payment_intent.succeeded" ->
						Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.PAID));
				case "payment_intent.payment_failed" ->
						Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.FAILED));
				default -> Optional.empty();
			};

		} catch (SignatureVerificationException e) {
			throw new PaymentException("Invalid Signature");
		}
	}

	private Long extractOrderId(Event event) {
		var stripeObject = event.getDataObjectDeserializer().getObject().orElseThrow(
				() -> new PaymentException("Could not Deserialize Stripe event."));
		var paymentIntent = (PaymentIntent) stripeObject;
		return Long.valueOf(paymentIntent.getMetadata().get("order_id"));
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
