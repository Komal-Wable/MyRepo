package com.example.lms_course_service.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseRequestDTO {


    @NotBlank(message = "Course title is required")
    private String title;

    @NotBlank(message = "Course description is required")
    private String description;


}
