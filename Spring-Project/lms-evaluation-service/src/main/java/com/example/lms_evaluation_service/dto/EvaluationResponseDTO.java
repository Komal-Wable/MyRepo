package com.example.lms_evaluation_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EvaluationResponseDTO {

    private Long id;
    private Long submissionId;
    private Long studentId;
    private Long assignmentId;

    private Integer marks;
    private String feedback;
    private String grade;

    private boolean graded;

    private String scorecardFileName;
}
