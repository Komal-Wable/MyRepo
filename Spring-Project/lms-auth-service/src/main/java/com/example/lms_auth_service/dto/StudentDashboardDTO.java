package com.example.lms_auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentDashboardDTO {

    private long enrolledCourses;
    private long pendingAssignments;
    private long submittedAssignments;
    private long evaluatedAssignments;

    private List<SubmissionTrendDTO> submissionTrend;

}
