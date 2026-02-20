package com.example.lms_course_service.kafka;

import com.example.events.CourseDeletedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CourseEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public CourseEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishCourseDeleted(Long courseId) {

        CourseDeletedEvent event =
                new CourseDeletedEvent(courseId);

        kafkaTemplate.send("course-deleted", event);

        System.out.println("COURSE_DELETED event published for courseId = " + courseId);
    }
}
