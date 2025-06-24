package com.projit.storeApp.mapper;

import com.projit.storeApp.dtos.CartDto;
import com.projit.storeApp.entities.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
	CartDto toDto(Cart cart);
}
