package com.example.lms_evaluation_service.dto;

import lombok.Data;

@Data
public class EvaluationResponseStudentDTO {

    private Long id;
    private String assignmentName;
    private String courseName;
    private Integer marks;
    private String grade;
    private String feedback;
    private Long submissionId;
}
