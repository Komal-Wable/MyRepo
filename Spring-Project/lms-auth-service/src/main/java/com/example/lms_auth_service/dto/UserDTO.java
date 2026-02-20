package com.example.lms_auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private String role;
}
