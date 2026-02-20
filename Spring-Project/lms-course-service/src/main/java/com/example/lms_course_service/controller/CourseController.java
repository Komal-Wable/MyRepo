package com.example.lms_course_service.controller;

import com.example.lms_course_service.dto.CourseListResponseDTO;
import com.example.lms_course_service.dto.CourseRequestDTO;
import com.example.lms_course_service.dto.CourseResponseDTO;
import com.example.lms_course_service.repository.CourseEnrollmentRepository;
import com.example.lms_course_service.repository.CourseRepository;
import com.example.lms_course_service.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CourseController {

    private final CourseService courseService;
    private final CourseEnrollmentRepository repo;
    public CourseController(CourseService courseService,CourseEnrollmentRepository repo) {
        this.courseService = courseService;
        this.repo=repo;
    }


    @PostMapping("/teacher/courses")
    public CourseResponseDTO createCourse(
            @Valid @RequestBody CourseRequestDTO dto,
            @RequestHeader("X-User-Id") Long teacherId) {

        return courseService.createCourse(dto, teacherId);
    }


    @GetMapping("/student/courses")
    public List<CourseListResponseDTO> getAllCourses() {
        return courseService.getAllCourses();
    }

    @DeleteMapping("/teacher/courses/{courseId}")
    public ResponseEntity<String> deleteCourse(
            @PathVariable Long courseId,
            @RequestHeader("X-User-Id") Long teacherId) {

        courseService.softDeleteCourse(courseId, teacherId);

        return ResponseEntity.ok("Course deleted successfully");
    }

    //@GetMapping("/internal/courses/{id}")
    public ResponseEntity<Void> courseExists(@PathVariable Long id) {

        courseService.getCourseById(id); // throws if not found
        return ResponseEntity.ok().build();
    }


    @PostMapping("/student/courses/{courseId}/enroll")
    public ResponseEntity<String> enrollCourse(
            @PathVariable Long courseId,
            @RequestHeader("X-User-Id") Long studentId) {
        System.out.println("StudentId = " + studentId);
        courseService.enrollStudent(courseId, studentId);

        return ResponseEntity.ok("Enrolled successfully");
    }

    @GetMapping("/internal/enrollments/check")
    public ResponseEntity<Boolean> checkEnrollment(
            @RequestParam Long courseId,
            @RequestParam Long studentId) {

        boolean enrolled =
                courseService.isStudentEnrolled(courseId, studentId);

        return ResponseEntity.ok(enrolled);
    }
    @GetMapping("/teacher/courses")
    public List<CourseListResponseDTO> getTeacherCourses(
            @RequestHeader("X-User-Id") Long teacherId) {

        return courseService.getTeacherCourses(teacherId);
    }

    @GetMapping("/student/courses/{courseId}/enrollment-status")
    public ResponseEntity<Boolean> checkMyEnrollment(
            @PathVariable Long courseId,
            @RequestHeader("X-User-Id") Long studentId
    ) {

        return ResponseEntity.ok(
                courseService.isStudentEnrolled(courseId, studentId)
        );
    }

    @GetMapping("/internal/courses/{id}")
    public CourseResponseDTO getCourseInternal(
            @PathVariable Long id) {

        return courseService.getCourseById(id);
    }
    @GetMapping("/internal/courses/count/{teacherId}")
    public long countCourses(@PathVariable Long teacherId){

        return courseService.countCourses(teacherId);
    }


    @GetMapping("/internal/courses/student/count/{studentId}")
    public long countEnrolled(
            @PathVariable Long studentId){

        return repo.countByStudentId(studentId);
    }
    @GetMapping("/internal/courses/student/{studentId}")
    public List<Long> getCourseIds(
            @PathVariable Long studentId){

        return repo.findCourseIdsByStudentId(studentId);
    }

}
