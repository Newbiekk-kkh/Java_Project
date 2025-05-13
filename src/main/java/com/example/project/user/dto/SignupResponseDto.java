package com.example.project.user.dto;

import com.example.project.user.entity.User;
import com.example.project.user.enums.Role;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SignupResponseDto {
    private final String username;
    private final String nickname;
    private final List<RoleDto> roles;

    public SignupResponseDto(String username, String nickname, List<RoleDto> roles) {
        this.username = username;
        this.nickname = nickname;
        this.roles = roles;
    }

    public static SignupResponseDto toDto(User user) {
        List<RoleDto> roles = user.getRoles()
                .stream()
                .map(RoleDto::new)
                .toList();
        return new SignupResponseDto(user.getUsername(), user.getNickname(), roles);
    }
}
