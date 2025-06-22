package com.projit.storeApp.mapper;

import com.projit.storeApp.dtos.RegisterUserRequest;
import com.projit.storeApp.dtos.UpdateUserRequest;
import com.projit.storeApp.dtos.UserDto;
import com.projit.storeApp.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
	UserDto toDto(User user);
	User toEntity(RegisterUserRequest request);
	void update(UpdateUserRequest request, @MappingTarget User user);
}
