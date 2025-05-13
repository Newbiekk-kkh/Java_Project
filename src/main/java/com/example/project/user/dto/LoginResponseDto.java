package com.example.project.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "로그인 응답 데이터 (JWT 토큰 포함)")
public class LoginResponseDto {
    @Schema(description = "발급된 JWT Access Token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMTIzIiwicm9sZXMiOiJVU0VSIiwiaWF0IjoxNj...")
    private String token;

    public LoginResponseDto(String token) {
        this.token = token;
    }
}
