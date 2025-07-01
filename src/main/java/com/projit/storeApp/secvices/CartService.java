package com.projit.storeApp.secvices;

import com.projit.storeApp.dtos.CartDto;
import com.projit.storeApp.dtos.CartItemDto;
import com.projit.storeApp.entities.Cart;
import com.projit.storeApp.exception.CartNotFoundException;
import com.projit.storeApp.exception.ProductNotFoundException;
import com.projit.storeApp.mapper.CartMapper;
import com.projit.storeApp.repositories.CartRepository;
import com.projit.storeApp.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Service
public class CartService {
	private CartRepository cartRepository;
	private CartMapper cartMapper;
	private ProductRepository productRepository;

	public CartDto createCart(){
		var cart = new Cart();
		cartRepository.save(cart);
		return cartMapper.toDto(cart);
	}

	public CartItemDto addToCart(UUID cartId, Long productId){
		var cart = cartRepository.getCartWithItems(cartId).orElse(null);
		if (cart == null) {
			throw new CartNotFoundException();
		}

		var product = productRepository.findById(productId).orElse(null);
		if (product == null) {
			throw new ProductNotFoundException();
		}

		var cartItem = cart.addItem(product);

		cartRepository.save(cart);
		return cartMapper.toDto(cartItem);
	}

	public CartDto getCart(UUID cartId){
		var cart = cartRepository.getCartWithItems(cartId).orElse(null);
		if (cart == null) {
			throw new CartNotFoundException();
		}
		return cartMapper.toDto(cart);
	}

	public CartItemDto updateCart(UUID cartId, Long productId, Integer quantity){
		var cart = cartRepository.getCartWithItems(cartId).orElse(null);
		if (cart == null) {
			throw new CartNotFoundException();
		}

		var cartItem = cart.getItem(productId);

		if (cartItem == null) {
			throw  new ProductNotFoundException();
		}

		cartItem.setQuantity(quantity);
		cartRepository.save(cart);
		return cartMapper.toDto(cartItem);
	}

	public void removeItem(UUID cartId, Long productId){
		var cart = cartRepository.getCartWithItems(cartId).orElse(null);
		if (cart == null) {
			throw new CartNotFoundException();
		}
		cart.removeItem(productId);
		cartRepository.save(cart);
	}

	public void clearCart(UUID cartId){
		var cart = cartRepository.getCartWithItems(cartId).orElse(null);
		if (cart == null) {
			ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					Map.of("error", "Cart not found")
			);
		}
		cart.clear();
		cartRepository.save(cart);
	}

	public List<CartDto> getAllCarts(){
		return cartRepository.findAll()
				.stream().map(cartMapper::toDto).toList();
	}
}
