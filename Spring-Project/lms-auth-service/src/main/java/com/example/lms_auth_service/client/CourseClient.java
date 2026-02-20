package com.example.lms_auth_service.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "LMS-COURSE-SERVICE")
public interface CourseClient {

    @GetMapping("/api/internal/courses/count/{teacherId}")
    long getCourseCount(@PathVariable Long teacherId);
    @GetMapping("/api/internal/courses/student/count/{studentId}")
    long getEnrolledCourses(@PathVariable Long studentId);
}
