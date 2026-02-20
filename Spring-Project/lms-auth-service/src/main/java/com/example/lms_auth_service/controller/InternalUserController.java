package com.example.lms_auth_service.controller;

import com.example.lms_auth_service.dto.UserDTO;
import com.example.lms_auth_service.entity.User;
import com.example.lms_auth_service.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internal/users")
public class InternalUserController {

    private final UserService userService;

    public InternalUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Long id) {

        User user = userService.getUserById(id);

        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}
