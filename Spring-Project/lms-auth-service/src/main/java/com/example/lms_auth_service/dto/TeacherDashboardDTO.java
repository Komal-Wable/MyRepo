package com.example.lms_auth_service.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDashboardDTO {


    private long courses;
    private long assignments;
    private long students;
    private long pendingEvaluations;
    private List<SubmissionTrendDTO> submissionTrend;
}
