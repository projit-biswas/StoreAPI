package com.projit.storeApp.controllers;

import com.projit.storeApp.dtos.CheckoutRequest;
import com.projit.storeApp.dtos.CheckoutResponse;
import com.projit.storeApp.dtos.ErrorDto;
import com.projit.storeApp.exception.CartEmptyException;
import com.projit.storeApp.exception.CartNotFoundException;
import com.projit.storeApp.exception.PaymentException;
import com.projit.storeApp.repositories.OrderRepository;
import com.projit.storeApp.services.CheckoutService;
import com.projit.storeApp.services.WebhookRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
@Tag(name = "Checkout")
public class CheckoutController {
	private final CheckoutService checkoutService;
	private final OrderRepository orderRepository;

	@PostMapping
	public CheckoutResponse checkout(@Valid @RequestBody CheckoutRequest request) {
		return checkoutService.checkout(request);
	}

	@PostMapping("/webhook")
	public void handleWebhook(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
		checkoutService.handleWebhook(new WebhookRequest(headers, payload));
	}

	@ExceptionHandler(PaymentException.class)
	public ResponseEntity<ErrorDto> handlePaymentException() {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				new ErrorDto("Internal Server Error")
		);
	}

	@ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
	public ResponseEntity<ErrorDto> handleException(Exception ex) {
		return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
	}

}

