package com.example.lms_submission_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubmissionTrendDTO {


    private String day;
    private Long submissions;
}
