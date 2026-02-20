package com.example.lms_submission_service.client;

import com.example.lms_submission_service.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "LMS-AUTH-SERVICE")
public interface UserClient {

    @GetMapping("/api/internal/users/{id}")
    UserDTO getUser(@PathVariable Long id);
}
