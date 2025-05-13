package com.example.project.user.entity;

import com.example.project.user.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class User {
    @Setter
    private Long id;

    private String username;
    private String password;
    private String nickname;
    private List<Role> roles;

    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.roles = new ArrayList<>();
        this.roles.add(Role.USER);
    }

}
