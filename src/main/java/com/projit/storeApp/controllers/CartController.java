package com.projit.storeApp.controllers;

import com.projit.storeApp.dtos.AddItemToCartRequest;
import com.projit.storeApp.dtos.CartDto;
import com.projit.storeApp.entities.Cart;
import com.projit.storeApp.entities.CartItem;
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

		var cartItem = cart.getItems().stream()
				.filter(items -> items.getProduct().getId().equals(product.getId()))
				.findFirst()
				.orElse(null);

		if (cartItem != null) {
			cartItem.setQuantity(cartItem.getQuantity() + 1);
		}else {
			cartItem = new CartItem();
			cartItem.setProduct(product);
			cartItem.setCart(cart);
			cartItem.setQuantity(1);
			cart.getItems().add(cartItem);
		}
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
}
