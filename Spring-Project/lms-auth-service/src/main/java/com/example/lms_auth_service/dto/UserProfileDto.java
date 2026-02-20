package com.example.lms_auth_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileDto {

    private Long id;
    private String name;
    private String email;
    private String role;
    private String joinedAt;
}
