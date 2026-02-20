package com.example.lms_submission_service.kafka;

import com.example.events.SubmissionCreatedEvent;
import com.example.events.SubmissionDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubmissionEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;


    public void publishSubmissionCreated(
            SubmissionCreatedEvent event) {

        kafkaTemplate.send(
                "submission-created",
                event
        ).whenComplete((result, ex) -> {

            if (ex == null) {
                System.out.println(
                        " SubmissionCreatedEvent SENT → "
                                + event.getSubmissionId());
            } else {
                System.out.println(
                        " Failed to send created event: "
                                + ex.getMessage());
            }
        });
    }




    public void publishSubmissionDeleted(
            Long submissionId) {

        SubmissionDeletedEvent event =
                new SubmissionDeletedEvent(submissionId);

        kafkaTemplate.send(
                "submission-deleted",
                event
        ).whenComplete((result, ex) -> {

            if (ex == null) {
                System.out.println(
                        "🔥 SubmissionDeletedEvent SENT → "
                                + submissionId);
            } else {
                System.out.println(
                        "❌ Failed to send delete event: "
                                + ex.getMessage());
            }
        });
    }
}
