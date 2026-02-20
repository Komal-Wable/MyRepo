package com.example.lms_auth_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "LMS-ASSIGNMENT-SERVICE")
public interface AssignmentClient {

    @GetMapping("/api/internal/assignments/count/{teacherId}")
    long getAssignmentCount(@PathVariable Long teacherId);

    @GetMapping("/api/internal/assignments/student/pending/{studentId}")
    long getPendingAssignments(@PathVariable Long studentId);

}
