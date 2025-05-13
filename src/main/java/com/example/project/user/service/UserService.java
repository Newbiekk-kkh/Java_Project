package com.example.project.user.service;

import com.example.project.exception.AuthException;
import com.example.project.exception.ErrorCode;
import com.example.project.user.dto.AdminRoleResponseDto;
import com.example.project.user.dto.SignupRequestDto;
import com.example.project.user.dto.SignupResponseDto;
import com.example.project.user.entity.User;
import com.example.project.user.enums.Role;
import com.example.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public SignupResponseDto signup(SignupRequestDto requestDto) {
        if (userRepository.existByUsername(requestDto.getUsername())) {
            throw new AuthException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = new User(requestDto.getUsername(), requestDto.getPassword(), requestDto.getNickname());
        userRepository.save(user);

        return SignupResponseDto.toDto(user);
    }

    public AdminRoleResponseDto assignAdminRole(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AuthException(ErrorCode.USER_NOT_FOUND));

        user.updateRoles(Role.ADMIN);

        return AdminRoleResponseDto.toDto(user);
    }
}
