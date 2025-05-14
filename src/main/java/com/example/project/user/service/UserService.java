package com.example.project.user.service;

import com.example.project.exception.AuthException;
import com.example.project.exception.ErrorCode;
import com.example.project.user.dto.*;
import com.example.project.user.entity.User;
import com.example.project.user.enums.Role;
import com.example.project.user.repository.UserRepository;
import com.example.project.utils.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    public SignupResponseDto signup(SignupRequestDto requestDto) {
        if (userRepository.existByUsername(requestDto.getUsername())) {
            throw new AuthException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = new User(requestDto.getUsername(), passwordEncoder.encode(requestDto.getPassword()), requestDto.getNickname());
        userRepository.save(user);

        return SignupResponseDto.toDto(user);
    }

    public AdminRoleResponseDto assignAdminRole(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AuthException(ErrorCode.USER_NOT_FOUND));

        user.updateRoles(Role.ADMIN);
        userRepository.save(user);

        return AdminRoleResponseDto.toDto(user);
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword()
                )
        );

        String token = jwtProvider.generateToken(authentication);
        return new LoginResponseDto(token);
    }
}
