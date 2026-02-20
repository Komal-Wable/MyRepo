package com.example.lms_auth_service;

import com.example.lms_auth_service.controller.AuthController;
import com.example.lms_auth_service.dto.*;
import com.example.lms_auth_service.entity.Role;
import com.example.lms_auth_service.entity.User;
import com.example.lms_auth_service.security.jwt.JwtUtil;
import com.example.lms_auth_service.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    private MockMvc mockMvc;

    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private UserService userService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {

        authenticationManager = mock(AuthenticationManager.class);
        jwtUtil = mock(JwtUtil.class);
        userService = mock(UserService.class);

        AuthController controller =
                new AuthController(
                        authenticationManager,
                        jwtUtil,
                        userService
                );

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        objectMapper = new ObjectMapper();
    }


    @Test
    void register_shouldReturnUser() throws Exception {

        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setName("Komal");
        dto.setEmail("komal@gmail.com");
        dto.setPassword("1234");
        dto.setRole("ROLE_STUDENT");

        User user = new User();
        user.setId(1L);
        user.setName("Komal");
        user.setEmail("komal@gmail.com");
        user.setRole(Role.ROLE_STUDENT);

        when(userService.register(any()))
                .thenReturn(user);

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email")
                        .value("komal@gmail.com"));
    }


    @Test
    void login_shouldReturnToken() throws Exception {

        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("komal@gmail.com");
        dto.setPassword("1234");

        User user = new User();
        user.setId(1L);
        user.setEmail("komal@gmail.com");
        user.setRole(Role.ROLE_STUDENT);

        when(userService.findByEmail(dto.getEmail()))
                .thenReturn(user);

        when(jwtUtil.generateToken(any(), any(), any()))
                .thenReturn("fake-jwt-token");

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token")
                        .value("fake-jwt-token"));

        verify(authenticationManager)
                .authenticate(any());
    }
}
