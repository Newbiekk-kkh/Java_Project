package com.example.project.user.dto;

import com.example.project.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "회원가입 응답 데이터")
public class SignupResponseDto {

    @Schema(description = "사용자 아이디", example = "user123")
    private final String username;

    @Schema(description = "사용자 닉네임", example = "멋진닉네임")
    private final String nickname;

    @Schema(description = "사용자 역할", example = "[\"USER\"]")
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
