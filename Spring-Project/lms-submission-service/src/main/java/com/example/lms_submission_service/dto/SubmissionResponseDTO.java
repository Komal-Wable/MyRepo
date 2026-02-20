package com.example.lms_submission_service.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class SubmissionResponseDTO {

    private Long id;
    private Long assignmentId;
    private Long studentId;
    private String fileName;
    private LocalDateTime submittedAt;
    private String studentName;

    private Integer marks;
    private String feedback;
    private boolean graded;
    private String submissionType;
    private String linkUrl;
    //private String fileName;


    public SubmissionResponseDTO(Long id, Long studentId, String fileName, LocalDateTime submittedAt) {
        this.id = id;
        this.studentId = studentId;
        this.fileName = fileName;
        this.submittedAt = submittedAt;
    }
}
