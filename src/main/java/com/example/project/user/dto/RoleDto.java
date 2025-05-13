package com.example.project.user.dto;

import com.example.project.user.enums.Role;
import lombok.Getter;

@Getter
public class RoleDto {
    private Role role;

    public RoleDto(Role role) {
        this.role = role;
    }
}
