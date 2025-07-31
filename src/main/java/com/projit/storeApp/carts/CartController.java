package com.projit.storeApp.carts;

import com.projit.storeApp.common.ErrorDto;
import com.projit.storeApp.products.ProductNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/carts")
@Tag(name = "Carts")
public class CartController {

	private CartService cartService;

	@PostMapping
	public ResponseEntity<CartDto> createCart(UriComponentsBuilder builder){
		var cartDto = cartService.createCart();
		var uri = builder.path("/api/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
		return ResponseEntity.created(uri).body(cartDto);
	}

	@PostMapping("/{cartId}/items")
	public ResponseEntity<?> addToCart(@PathVariable UUID cartId, @Valid @RequestBody AddItemToCartRequest request){
		var cartItemDto = cartService.addToCart(cartId, request.getProductId());
		return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
	}

	@GetMapping
	public List<CartDto> getaAllCarts(){
		return cartService.getAllCarts();
	}

	@GetMapping("/{cartId}")
	public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId){
		var cartDto = cartService.getCart(cartId);
		return ResponseEntity.ok(cartDto);
	}

	@PutMapping("/{cartId}/items/{productId}")
	public ResponseEntity<?> updateItem(
			@PathVariable("cartId") UUID cartId,
			@PathVariable("productId") Long productId,
			@Valid @RequestBody UpdateCartItemRequest request)
	{

		var cartItemDto = cartService.updateCart(cartId, productId, request.getQuantity());
		return ResponseEntity.ok(cartItemDto);
	}

	@DeleteMapping("/{cartId}/items/{productId}")
	public ResponseEntity<?> removeItem(
			@PathVariable("cartId") UUID cartId,
			@PathVariable("productId") Long productId
	){
		cartService.removeItem(cartId,productId);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{cartId}/items")
	public ResponseEntity<Void> clearCart(@PathVariable UUID cartId){
		cartService.clearCart(cartId);
		return ResponseEntity.noContent().build();
	}

	@ExceptionHandler(CartNotFoundException.class)
	public ResponseEntity<ErrorDto> handleCartNotFound() {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new ErrorDto("Cart Not Found")
		);
	}

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<ErrorDto> handleProductNotFound() {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new ErrorDto("Product not found in the cart")
		);
	}
}
