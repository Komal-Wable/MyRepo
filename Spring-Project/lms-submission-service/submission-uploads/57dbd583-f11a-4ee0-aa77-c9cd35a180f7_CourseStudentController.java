package com.quiz.quiz.controller;

import com.quiz.quiz.dto.EnrollmentResponseDTO;
import com.quiz.quiz.entity.CourseStudent;
import com.quiz.quiz.service.CourseStudentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/enrollments")
@Tag(name = "Student - Enrollment")
public class CourseStudentController {

    private final CourseStudentService courseStudentService;

    public CourseStudentController(CourseStudentService courseStudentService) {
        this.courseStudentService = courseStudentService;
    }

    @PostMapping
    public EnrollmentResponseDTO joinCourse(
            @RequestParam Long courseId,
            Authentication authentication) {
        String studentEmail = authentication.getName();
        return courseStudentService.joinCourse(courseId, studentEmail);
    }

}
