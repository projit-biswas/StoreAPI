package com.projit.storeApp.controllers;

import com.projit.storeApp.dtos.CartDto;
import com.projit.storeApp.entities.Cart;
import com.projit.storeApp.mapper.CartMapper;
import com.projit.storeApp.repositories.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@RestController
@RequestMapping("/api/carts")
public class CartController {

	private final CartRepository cartRepository;
	private final CartMapper cartMapper;

	@PostMapping
	public ResponseEntity<CartDto> createCart(UriComponentsBuilder builder){
		var cart = new Cart();
		cartRepository.save(cart);
		var cartDto = cartMapper.toDto(cart);
		var uri = builder.path("/api/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
		return ResponseEntity.created(uri).body(cartDto);
	}
}
