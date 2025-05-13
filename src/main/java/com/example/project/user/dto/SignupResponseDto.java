package com.example.project.user.dto;

import com.example.project.user.enums.Role;
import lombok.Getter;

import java.util.List;

@Getter
public class SignupResponseDto {
    private final String username;
    private final String nickname;
    private final List<Role> roles;

    public SignupResponseDto(String username, String nickname, List<Role> roles) {
        this.username = username;
        this.nickname = nickname;
        this.roles = roles;
    }
}
