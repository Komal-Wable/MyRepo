package com.example.lms_submission_service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AssignmentDTO {

    private Long id;
    private LocalDateTime dueDate;
    private String assignmentType;
    private Long teacherId;
}
