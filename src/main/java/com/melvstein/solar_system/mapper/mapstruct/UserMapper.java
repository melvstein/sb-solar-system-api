package com.melvstein.solar_system.mapper.mapstruct;

import com.melvstein.solar_system.dto.UserDto;
import com.melvstein.solar_system.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto dto);

    List<UserDto> toDtos(List<User> users);
    List<User> toEntities(List<UserDto> dtos);
}
