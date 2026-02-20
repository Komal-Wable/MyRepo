package com.example.lms_auth_service.controller;

import com.example.lms_auth_service.dto.*;
import com.example.lms_auth_service.entity.User;
import com.example.lms_auth_service.security.jwt.JwtUtil;
import com.example.lms_auth_service.service.UserService;
import jakarta.validation.Valid;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

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
    public UserResponseDTO register( @Valid @RequestBody RegisterRequestDTO dto) {
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
                        dto.getEmail(),
                        dto.getPassword()
                )
        );


        User user = userService.findByEmail(dto.getEmail());


        String token = jwtUtil.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );




        return new LoginResponseDTO(token);
    }


//    @GetMapping("/{id}")
//    public UserDTO getUser(@PathVariable Long id) {
//
//        User user = userService.getUserById(id);
//
//        return new UserDTO(
//                user.getId(),
//                user.getName(),
//                user.getEmail(),
//                user.getRole().name()
//        );
//    }
}
