package com.projit.storeApp.orders;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
	OrderDto toDto(Order order);
}
