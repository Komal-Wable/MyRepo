package com.example.lms_evaluation_service.repository;

import com.example.lms_evaluation_service.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EvaluationRepository
        extends JpaRepository<Evaluation, Long> {

    Optional<Evaluation> findBySubmissionId(Long submissionId);

    Optional<Evaluation> findBySubmissionIdAndDeletedFalse(Long submissionId);

    List<Evaluation> findByStudentIdAndDeletedFalse(Long studentId);
    List<Evaluation> findByStudentIdAndGradedTrue(Long studentId);

    boolean existsBySubmissionIdAndGradedTrue(
            Long submissionId);
    long countByTeacherIdAndGradedFalse(Long teacherId);
    void deleteBySubmissionId(Long submissionId);
    long countByStudentIdAndGradedTrue(Long studentId);

}
