package com.example.project.user.controller;

import com.example.project.user.dto.AdminRoleResponseDto;
import com.example.project.user.dto.SignupRequestDto;
import com.example.project.user.dto.SignupResponseDto;
import com.example.project.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto requestDto) {
        SignupResponseDto responseDto = userService.signup(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PatchMapping("/admin/users/{userId}/roles")
    public ResponseEntity<AdminRoleResponseDto> assignAdminRole(@PathVariable Long userId) {
        AdminRoleResponseDto responseDto = userService.assignAdminRole(userId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
