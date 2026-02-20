package com.example.lms_evaluation_service.client;

import com.example.lms_evaluation_service.dto.CourseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "LMS-COURSE-SERVICE")
public interface CourseClient {

    @GetMapping("/api/internal/courses/{id}")
    CourseDTO getCourse(@PathVariable Long id);
}
