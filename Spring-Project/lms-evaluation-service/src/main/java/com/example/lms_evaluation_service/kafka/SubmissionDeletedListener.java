package com.example.lms_evaluation_service.kafka;

import com.example.events.SubmissionDeletedEvent;
import com.example.lms_evaluation_service.repository.EvaluationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component

@RequiredArgsConstructor
public class SubmissionDeletedListener {

    private final EvaluationRepository repo;

    @KafkaListener(
            topics = "submission-deleted",
            groupId = "evaluation-group"
    )
    @Transactional
    public void handleSubmissionDeleted(
            SubmissionDeletedEvent event
    ) {

        System.out.println(
                "🔥 SubmissionDeletedEvent RECEIVED → "
                        + event.getSubmissionId()
        );

        repo.deleteBySubmissionId(
                event.getSubmissionId()
        );
    }
}
