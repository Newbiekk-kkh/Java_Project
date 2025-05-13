package com.example.project.user.controller;

import com.example.project.user.dto.*;
import com.example.project.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 회원가입을 처리합니다.
     * @param requestDto 회원가입할 정보(username, password, nickname) 을 담고 있습니다.
     * @return 회원가입 처리 결과를 담은 ResponseEntity
     */
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        SignupResponseDto responseDto = userService.signup(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * 로그인을 처리합니다.
     * @param loginRequestDto 로그인할 정보(username, password) 를 담고 있습니다.
     * @return 로그인 성공시 accessToken 반환
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto responseDto = userService.login(loginRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 사용자의 Role 을 ADMIN 으로 변경합니다.
     * @param userId 변경할 유저 id 입니다.
     * @return Role 변경 내용을 담은 ResponseEntity
     */
    @PatchMapping("/admin/users/{userId}/roles")
    public ResponseEntity<AdminRoleResponseDto> assignAdminRole(@PathVariable Long userId) {
        AdminRoleResponseDto responseDto = userService.assignAdminRole(userId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
