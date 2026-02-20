package com.example.lms_auth_service.controller;

import com.example.lms_auth_service.dto.StudentDashboardDTO;
import com.example.lms_auth_service.dto.TeacherDashboardDTO;
import com.example.lms_auth_service.service.DashboardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TeacherDashboardController {
    private final DashboardService dashboardService;

    public TeacherDashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/teacher/dashboard")
    public TeacherDashboardDTO getStats(@RequestHeader("X-User-Id") Long teacherId) {

        return dashboardService.getTeacherStats(teacherId);



    }
    @GetMapping("/student/dashboard")
    public StudentDashboardDTO studentDashboard(
            @RequestHeader("X-User-Id") Long studentId){

        return dashboardService.getStudentStats(studentId);
    }

}
