package com.example.service;

import com.example.dto.request.UserRegistrationRequestDto;
import com.example.dto.response.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto userRequestDto);
}
