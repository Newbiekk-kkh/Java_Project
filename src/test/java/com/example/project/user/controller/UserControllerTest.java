package com.example.project.user.controller;

import com.example.project.config.auth.UserDetailsServiceImpl;
import com.example.project.config.filter.SecurityConfig;
import com.example.project.config.handler.CustomAccessDeniedHandler;
import com.example.project.config.handler.CustomAuthenticationEntryPoint;
import com.example.project.exception.AuthException;
import com.example.project.exception.ErrorCode;
import com.example.project.user.dto.*;
import com.example.project.user.enums.Role;
import com.example.project.user.service.UserService;
import com.example.project.utils.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @MockitoBean
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @MockitoBean
    private UserDetailsServiceImpl userDetailService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    public void signupSuccessTest() throws Exception {
        // Given
        SignupRequestDto requestDto = new SignupRequestDto("testuser", "password123", "테스트유저");
        List<RoleDto> expectedRoles = List.of(new RoleDto(Role.USER));
        SignupResponseDto responseDto = new SignupResponseDto("testuser", "테스트유저", expectedRoles);

        when(userService.signup(any(SignupRequestDto.class))).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roles").isArray())
                .andExpect(jsonPath("$.roles.length()").value(1))
                .andExpect(jsonPath("$.roles[0].role").value(Role.USER.name()))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.nickname").value("테스트유저"));
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 이미 존재하는 사용자")
    public void signupFailureTest() throws Exception {
        // Given
        SignupRequestDto requestDto = new SignupRequestDto("existinguser", "password123", "기존유저");

        when(userService.signup(any(SignupRequestDto.class)))
                .thenThrow(new AuthException(ErrorCode.USER_ALREADY_EXISTS));

        // When & Then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("USER_ALREADY_EXISTS"));
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception {
        // Given
        LoginRequestDto requestDto = new LoginRequestDto("testuser", "password123");
        LoginResponseDto responseDto = new LoginResponseDto("test.jwt.token");

        when(userService.login(any(LoginRequestDto.class))).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test.jwt.token"));
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 잘못된 인증 정보")
    public void loginFailureTest() throws Exception {
        // Given
        LoginRequestDto requestDto = new LoginRequestDto("testuser", "wrongpassword");

        when(userService.login(any(LoginRequestDto.class)))
                .thenThrow(new AuthException(ErrorCode.INVALID_CREDENTIALS));

        // When & Then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error.code").value(ErrorCode.INVALID_CREDENTIALS.getCode()));
    }

    @Test
    @DisplayName("관리자 권한 부여 성공 테스트")
    @WithMockUser(roles = "ADMIN")
    public void assignAdminRoleSuccessTest() throws Exception {
        // Given
        Long userId = 1L;
        List<RoleDto> expectedRoles = List.of(new RoleDto(Role.ADMIN));
        AdminRoleResponseDto responseDto = new AdminRoleResponseDto("testuser", "테스트유저", expectedRoles);

        when(userService.assignAdminRole(userId)).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(patch("/admin/users/{userId}/roles", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.nickname").value("테스트유저"))
                .andExpect(jsonPath("$.roles").isArray())
                .andExpect(jsonPath("$.roles.length()").value(1))
                .andExpect(jsonPath("$.roles[0].role").value(Role.ADMIN.name()));
    }

    @Test
    @DisplayName("관리자 권한 부여 실패 테스트 - 사용자 없음")
    @WithMockUser(roles = "ADMIN")
    public void assignAdminRoleUserNotFoundTest() throws Exception {
        // Given
        Long userId = 999L;

        when(userService.assignAdminRole(userId))
                .thenThrow(new AuthException(ErrorCode.USER_NOT_FOUND));

        // When & Then
        mockMvc.perform(patch("/admin/users/{userId}/roles", userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.code").value("USER_NOT_FOUND"));
    }
}