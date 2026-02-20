package com.example.lms_auth_service.service;

import com.example.lms_auth_service.client.AssignmentClient;
import com.example.lms_auth_service.client.CourseClient;
import com.example.lms_auth_service.client.EvaluationClient;
import com.example.lms_auth_service.client.SubmissionClient;
import com.example.lms_auth_service.dto.SubmissionTrendDTO;
import com.example.lms_auth_service.dto.TeacherDashboardDTO;
import lombok.RequiredArgsConstructor;
import com.example.lms_auth_service.dto.StudentDashboardDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final CourseClient courseClient;
    private final AssignmentClient assignmentClient;
    private final SubmissionClient submissionClient;
    private final EvaluationClient evaluationClient;

    public TeacherDashboardDTO getTeacherStats(Long teacherId) {

        long courses = courseClient.getCourseCount(teacherId);

        long assignments = assignmentClient.getAssignmentCount(teacherId);

        long students = submissionClient.getSubmissionCount(teacherId);

        long pending =
                evaluationClient.getPendingEvaluations(teacherId);
        var trend =
                submissionClient.getTrend(teacherId);
        return new TeacherDashboardDTO(
                courses,
                assignments,
                students,
                pending,
                trend
        );
    }


    public StudentDashboardDTO getStudentStats(Long studentId){

    long courses =
            courseClient.getEnrolledCourses(studentId);

    long pending =
            assignmentClient.getPendingAssignments(studentId);

    long submitted =
            submissionClient.getSubmissionCountStudent(studentId);

    long evaluated =
            evaluationClient.getEvaluatedCount(studentId);

        return StudentDashboardDTO.builder()
                .enrolledCourses(courses)
                .pendingAssignments(pending)
                .submittedAssignments(submitted)
                .evaluatedAssignments(evaluated)
                .submissionTrend(
                        submissionClient.getTrendStudent(studentId)
                )
                .build();

}



}
