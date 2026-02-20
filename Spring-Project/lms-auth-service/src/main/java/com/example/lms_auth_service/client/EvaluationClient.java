package com.example.lms_auth_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "LMS-EVALUATION-SERVICE")
public interface EvaluationClient {

    @GetMapping("/api/internal/evaluations/pending/{teacherId}")
    long getPendingEvaluations(@PathVariable Long teacherId);

    @GetMapping("/api/internal/evaluations/student/count/{studentId}")
    long getEvaluatedCount(@PathVariable Long studentId);

}
