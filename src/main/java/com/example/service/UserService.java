package com.example.service;

import com.example.config.dto.request.UserRegistrationRequestDto;
import com.example.config.dto.response.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto userRequestDto);
}
