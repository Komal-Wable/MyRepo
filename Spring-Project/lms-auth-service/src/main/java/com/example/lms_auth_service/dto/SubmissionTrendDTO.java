package com.example.lms_auth_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubmissionTrendDTO {
    private String day;
    private int submissions;

}
