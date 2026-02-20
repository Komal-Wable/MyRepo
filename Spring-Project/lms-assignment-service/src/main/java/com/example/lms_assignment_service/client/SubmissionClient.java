package com.example.lms_assignment_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "lms-submission-service")
public interface SubmissionClient {

    @GetMapping("/api/internal/submissions/student/{studentId}")
    List<Long> getSubmittedAssignmentIds(@PathVariable Long studentId);
}