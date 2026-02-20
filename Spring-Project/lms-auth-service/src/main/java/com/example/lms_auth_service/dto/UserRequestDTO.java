package com.example.lms_auth_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {

    private String name;
    private String email;
    private String password;
    private String role; // TEACHER / STUDENT

    // getters & setters
}
