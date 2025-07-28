package com.projit.storeApp.controllers;

import com.projit.storeApp.dtos.ErrorDto;
import com.projit.storeApp.dtos.OrderDto;
import com.projit.storeApp.exception.OrderNotFoundException;
import com.projit.storeApp.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

	private final OrderService orderService;

	@GetMapping
	public List<OrderDto> getAllOrders() {
		return orderService.getAllOrders();
	}

	@GetMapping("/{orderId}")
	public OrderDto getOrderById(@PathVariable("orderId") Long orderId) {
		return orderService.getOrderById(orderId);
	}

	@ExceptionHandler(OrderNotFoundException.class)
	public ResponseEntity<Void> handleOrderNotFound() {
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorDto> handleAccessDenied(AccessDeniedException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDto(ex.getMessage()));
	}
}
