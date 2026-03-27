package com.example.service.impl;

import com.example.dto.UserRegistrationRequestDto;
import com.example.dto.UserResponseDto;
import com.example.dto.mapping.UserMapper;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.exception.RegistrationException;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto userRequestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new RegistrationException(
                    "Email already exists: " + userRequestDto.getEmail()
            );
        }

        User user = userMapper.toEntity(userRequestDto);
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        Role userRole = roleRepository.findByRoleName(Role.RoleName.USER)
                .orElseThrow(
                        () -> new RuntimeException("Role " + Role.RoleName.USER + "  not found")
                );
        user.getRoles().add(userRole);
        userRepository.save(user);

        return userMapper.toDto(user);
    }
}
