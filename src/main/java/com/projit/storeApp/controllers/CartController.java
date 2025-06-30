package com.projit.storeApp.controllers;

import com.projit.storeApp.dtos.AddItemToCartRequest;
import com.projit.storeApp.dtos.CartDto;
import com.projit.storeApp.dtos.UpdateCartItemRequest;
import com.projit.storeApp.entities.Cart;
import com.projit.storeApp.mapper.CartMapper;
import com.projit.storeApp.repositories.CartRepository;
import com.projit.storeApp.repositories.ProductRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/carts")
public class CartController {

	private final CartRepository cartRepository;
	private final CartMapper cartMapper;
	private final ProductRepository productRepository;

	@PostMapping
	public ResponseEntity<CartDto> createCart(UriComponentsBuilder builder){
		var cart = new Cart();
		cartRepository.save(cart);
		var cartDto = cartMapper.toDto(cart);
		var uri = builder.path("/api/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
		return ResponseEntity.created(uri).body(cartDto);
	}

	@PostMapping("/{cartId}/items")
	public ResponseEntity<?> addToCart(@PathVariable UUID cartId, @Valid @RequestBody AddItemToCartRequest request){
		var cart = cartRepository.getCartWithItems(cartId).orElse(null);
		if (cart == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					Map.of("error", "Cart not found.")
			);
		}

		var product = productRepository.findById(request.getProductId()).orElse(null);
		if (product == null) {
			return ResponseEntity.badRequest().body(
					Map.of("error", "Product not found.")
			);
		}

		var cartItem = cart.addItem(product);

		cartRepository.save(cart);
		var cartItemDto = cartMapper.toDto(cartItem);
		return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
	}

	@GetMapping
	public List<CartDto> getaAllCarts(){
		return cartRepository.findAll()
				.stream().map(cartMapper::toDto).toList();

	}

	@GetMapping("/{cartId}")
	public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId){
		var cart = cartRepository.getCartWithItems(cartId).orElse(null);
		if (cart == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(cartMapper.toDto(cart));
	}

	@PutMapping("/{cartId}/items/{productId}")
	public ResponseEntity<?> updateItem(
			@PathVariable("cartId") UUID cartId,
			@PathVariable("productId") Long productId,
			@Valid @RequestBody UpdateCartItemRequest request){

		var cart = cartRepository.getCartWithItems(cartId).orElse(null);
		if (cart == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					Map.of("error", "Cart is not found")
			);
		}

		var cartItem = cart.getItem(productId);

		if (cartItem == null) {
			ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					Map.of("error", "Product was not found in the Cart")
			);
		}

		cartItem.setQuantity(request.getQuantity());
		cartRepository.save(cart);
		return ResponseEntity.ok(cartMapper.toDto(cartItem));
	}
}
