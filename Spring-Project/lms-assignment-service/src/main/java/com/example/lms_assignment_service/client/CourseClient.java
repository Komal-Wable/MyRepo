package com.example.lms_assignment_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "LMS-COURSE-SERVICE")
public interface CourseClient {


    @GetMapping("/api/internal/courses/{id}")
    void courseExists(@PathVariable Long id);



    @GetMapping("/api/internal/enrollments/check")
    Boolean checkEnrollment(
            @RequestParam Long courseId,
            @RequestParam Long studentId);



        @GetMapping("/api/internal/courses/student/{studentId}")
        List<Long> getEnrolledCourseIds(
                @PathVariable Long studentId);



}
