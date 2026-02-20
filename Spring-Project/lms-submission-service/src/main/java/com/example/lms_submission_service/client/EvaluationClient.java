package com.example.lms_submission_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "LMS-EVALUATION-SERVICE")
public interface EvaluationClient {

    @GetMapping("/api/internal/evaluations/submission/{submissionId}")
    Boolean isGraded(@PathVariable Long submissionId);
}
