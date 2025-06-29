package com.projit.storeApp.mapper;

import com.projit.storeApp.dtos.CartDto;
import com.projit.storeApp.dtos.CartItemDto;
import com.projit.storeApp.entities.Cart;
import com.projit.storeApp.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
	@Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
	CartDto toDto(Cart cart);

	@Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
	CartItemDto toDto(CartItem cartItem);
}
