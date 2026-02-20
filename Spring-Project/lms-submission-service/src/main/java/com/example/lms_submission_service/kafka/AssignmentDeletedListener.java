package com.example.lms_submission_service.kafka;

import com.example.events.AssignmentDeletedEvent;
import com.example.lms_submission_service.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssignmentDeletedListener {

    private final SubmissionRepository repo;
    private final SubmissionEventProducer producer;

    @KafkaListener(
            topics = "assignment-deleted",
            groupId = "submission-group"
    )
    public void handleAssignmentDeleted(AssignmentDeletedEvent event) {

        System.out.println(
                " AssignmentDeletedEvent RECEIVED → "
                        + event.getAssignmentId());

        var submissions =
                repo.findByAssignmentIdAndDeletedFalse(
                        event.getAssignmentId());


        System.out.println(
                "Total submissions found = "
                        + submissions.size());

        submissions.forEach(submission -> {


            if (!submission.isDeleted()) {

                repo.deleteById(submission.getId());


                producer.publishSubmissionDeleted(
                        submission.getId()
                );

                System.out.println(
                        "Submission soft deleted → "
                                + submission.getId());
            }
        });
    }
}
