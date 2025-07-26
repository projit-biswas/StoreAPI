package com.projit.storeApp.mapper;

import com.projit.storeApp.dtos.OrderDto;
import com.projit.storeApp.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
	OrderDto toDto(Order order);
}
