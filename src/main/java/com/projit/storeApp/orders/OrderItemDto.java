package com.projit.storeApp.orders;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
	private OrderProductDto product;
	private int quantity;
	private BigDecimal totalPrice;
}
