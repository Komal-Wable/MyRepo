package com.example.lms_assignment_service.kafka;

import com.example.events.AssignmentDeletedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AssignmentEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public AssignmentEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishAssignmentDeleted(Long assignmentId) {

        AssignmentDeletedEvent event =
                new AssignmentDeletedEvent(assignmentId);

        kafkaTemplate.send("assignment-deleted", event);

        System.out.println("ASSIGNMENT_DELETED event sent");
    }
}
