package com.quiz.quiz.controller;

import com.quiz.quiz.dto.*;
import com.quiz.quiz.entity.User;
import com.quiz.quiz.service.UserService;
import com.quiz.quiz.security.jwt.JwtUtil;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }


    @PostMapping("/register")
    public UserResponseDTO register(@Valid @RequestBody RegisterRequestDTO dto) {
        User user = userService.register(dto);

        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());

        return response;
    }



    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO dto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(), dto.getPassword()));

        String token = jwtUtil.generateToken(dto.getEmail());
        return new LoginResponseDTO(token);
    }
}
