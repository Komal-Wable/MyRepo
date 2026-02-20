package com.example.lms_auth_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String role;

    // getters & setters
}
