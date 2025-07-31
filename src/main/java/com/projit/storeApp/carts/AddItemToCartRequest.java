package com.projit.storeApp.carts;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddItemToCartRequest {
	@NotNull(message = "Product Id is required.")
	private Long productId;
}
