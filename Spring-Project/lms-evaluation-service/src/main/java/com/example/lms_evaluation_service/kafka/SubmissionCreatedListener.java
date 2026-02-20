package com.example.lms_evaluation_service.kafka;

import com.example.events.SubmissionCreatedEvent;
import com.example.lms_evaluation_service.entity.Evaluation;
import com.example.lms_evaluation_service.repository.EvaluationRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SubmissionCreatedListener {

    private final EvaluationRepository repo;

    public SubmissionCreatedListener(
            EvaluationRepository repo) {

        this.repo = repo;
    }

    @KafkaListener(
            topics = "submission-created",
            groupId = "evaluation-group"
    )
    @KafkaListener(topics = "submission-created")
    public void handleSubmissionCreated(SubmissionCreatedEvent event) {

        Evaluation evaluation = new Evaluation();

        evaluation.setSubmissionId(event.getSubmissionId());
        evaluation.setAssignmentId(event.getAssignmentId());
        evaluation.setStudentId(event.getStudentId());
        evaluation.setTeacherId(event.getTeacherId());

        evaluation.setGraded(false);
        evaluation.setDeleted(false);

        repo.save(evaluation);

        System.out.println("Evaluation auto-created for submission: "
                + event.getSubmissionId());
    }

}
