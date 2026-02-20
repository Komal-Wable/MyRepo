package com.example.lms_course_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponseDTO {

    private Long id;
    private String title;
    private String description;
    private Long teacherId;

    // getters & setters
}
