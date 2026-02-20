package com.example.lms_assignment_service.kafka;

import com.example.events.CourseDeletedEvent;
import com.example.events.AssignmentDeletedEvent;
import com.example.lms_assignment_service.entity.Assignment;
import com.example.lms_assignment_service.repository.AssignmentRepository;

import com.example.lms_assignment_service.service.AssignmentService;
import lombok.RequiredArgsConstructor;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class CourseDeletedListener {

    private final AssignmentService assignmentService;

    @KafkaListener(
            topics = "course-deleted",
            groupId = "assignment-group"
    )
    public void handleCourseDeleted(
            CourseDeletedEvent event
    ) {

        System.out.println(
                " COURSE DELETE RECEIVED → "
                        + event.getCourseId()
        );

        assignmentService.deleteAssignmentsByCourse(
                event.getCourseId()
        );
    }
}
