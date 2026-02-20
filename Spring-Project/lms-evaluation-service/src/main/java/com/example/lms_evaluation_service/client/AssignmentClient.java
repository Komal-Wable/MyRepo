package com.example.lms_evaluation_service.client;

import com.example.lms_evaluation_service.dto.AssignmentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "LMS-ASSIGNMENT-SERVICE")
public interface AssignmentClient {

    @GetMapping("/api/internal/assignments/{id}")
    AssignmentDTO getAssignment(@PathVariable Long id);
}
