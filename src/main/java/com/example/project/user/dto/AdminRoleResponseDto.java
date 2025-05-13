package com.example.project.user.dto;

import com.example.project.user.entity.User;
import lombok.Getter;

import java.util.List;

@Getter
public class AdminRoleResponseDto {
    private final String username;
    private final String nickname;
    private final List<RoleDto> roles;

    public AdminRoleResponseDto(String username, String nickname, List<RoleDto> roles) {
        this.username = username;
        this.nickname = nickname;
        this.roles = roles;
    }

    public static AdminRoleResponseDto toDto(User user) {
        List<RoleDto> roles = user.getRoles()
                .stream()
                .map(RoleDto::new)
                .toList();
        return new AdminRoleResponseDto(user.getUsername(), user.getNickname(), roles);
    }
}
