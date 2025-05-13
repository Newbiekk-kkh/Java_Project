package com.example.project.user.service;

import com.example.project.user.dto.SignupRequestDto;
import com.example.project.user.dto.SignupResponseDto;
import com.example.project.user.entity.User;
import com.example.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public SignupResponseDto signup(SignupRequestDto requestDto) {
        User user = new User(requestDto.getUsername(), requestDto.getPassword(), requestDto.getNickname());
        userRepository.save(user);

        return new SignupResponseDto(user.getUsername(), user.getNickname(), user.getRoles());
    }
}
