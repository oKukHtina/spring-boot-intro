package com.example.mapper;

import com.example.dto.request.UserRegistrationRequestDto;
import com.example.dto.response.UserResponseDto;
import com.example.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserRegistrationRequestDto userRegistrationRequestDto);
}
