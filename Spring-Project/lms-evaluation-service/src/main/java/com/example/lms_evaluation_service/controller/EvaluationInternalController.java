package com.example.lms_evaluation_service.controller;

import com.example.lms_evaluation_service.repository.EvaluationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/evaluations")
public class EvaluationInternalController {

    private final EvaluationRepository repo;

    public EvaluationInternalController(
            EvaluationRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/submission/{submissionId}")
    public Boolean isGraded(
            @PathVariable Long submissionId) {

        return repo.existsBySubmissionIdAndGradedTrue(
                submissionId
        );
    }

}
