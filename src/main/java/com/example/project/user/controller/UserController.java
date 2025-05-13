package com.example.project.user.controller;

import com.example.project.exception.ErrorResponse;
import com.example.project.user.dto.*;
import com.example.project.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Management", description = "사용자 회원가입, 로그인, 관리자 권한 부여 API")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 회원가입을 처리합니다.
     * @param requestDto 회원가입할 정보(username, password, nickname) 을 담고 있습니다.
     * @return 회원가입 처리 결과를 담은 ResponseEntity
     */
    @Operation(summary = "회원가입", description = "새로운 사용자를 시스템에 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SignupResponseDto.class))), // 실제 SignupResponseDto 사용
            @ApiResponse(responseCode = "400", description = "이미 존재하는 사용자 (예: USER_ALREADY_EXISTS)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
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
    @Operation(summary = "로그인", description = "사용자 로그인 후 JWT 토큰을 발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDto.class))), // 실제 LoginResponseDto 사용
            @ApiResponse(responseCode = "401", description = "인증 실패 (예: INVALID_CREDENTIALS)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
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
    @Operation(summary = "관리자 권한 부여", description = "특정 사용자에게 관리자(ADMIN) 권한을 부여합니다. (ADMIN 역할 필요)")
    // @SecurityRequirement(name = "bearerAuth") // 이 어노테이션은 OpenAPI Bean에서 전역 설정 시 생략 가능
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "권한 부여 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdminRoleResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않음 (토큰 누락 또는 유효하지 않은 토큰)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음 (ADMIN 역할이 아님)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/admin/users/{userId}/roles")
    public ResponseEntity<AdminRoleResponseDto> assignAdminRole(
            @Parameter(description = "관리자 권한을 부여할 사용자의 ID", required = true, example = "2")
            @PathVariable Long userId) {
        AdminRoleResponseDto responseDto = userService.assignAdminRole(userId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
