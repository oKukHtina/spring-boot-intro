package com.example.service.impl;

import com.example.dto.UserRegistrationRequestDto;
import com.example.dto.UserResponseDto;
import com.example.dto.mapping.UserMapper;
import com.example.entity.User;
import com.example.exception.RegistrationException;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto userRequestDto) {
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new RegistrationException(
                    "Email already exists: " + maskEmail(userRequestDto.getEmail())
            );
        }

        User user = userMapper.toEntity(userRequestDto);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    private String maskEmail(String email) {
        return email.replaceAll("(?<=.).(?=.*@)", "*");
    }
}
