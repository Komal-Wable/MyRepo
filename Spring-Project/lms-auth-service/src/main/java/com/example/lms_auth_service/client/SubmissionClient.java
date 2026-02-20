package com.example.lms_auth_service.client;

import com.example.lms_auth_service.dto.SubmissionTrendDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "LMS-SUBMISSION-SERVICE")
public interface SubmissionClient {

    @GetMapping("/api/internal/submissions/count/{teacherId}")
    long getSubmissionCount(@PathVariable Long teacherId);

    @GetMapping("/api/internal/submissions/trend/{teacherId}")
    List<SubmissionTrendDTO> getTrend(
            @PathVariable Long teacherId);

    @GetMapping("/api/internal/submissions/student/count/{studentId}")
    long getSubmissionCountStudent(@PathVariable Long studentId);

    @GetMapping("/api/internal/submissions/student/trend/{studentId}")
    List<SubmissionTrendDTO> getTrendStudent(@PathVariable Long studentId);

}
