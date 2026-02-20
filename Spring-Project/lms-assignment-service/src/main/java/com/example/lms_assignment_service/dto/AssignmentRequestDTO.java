package com.example.lms_assignment_service.dto;

import com.example.lms_assignment_service.entity.AssignmentType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

//import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class AssignmentRequestDTO {

    @NotBlank
    private String title;

    private String description;

    private AssignmentType assignmentType;

    private String instructionPathLink;

    private LocalDateTime dueDate;

    private Long courseId;
}
