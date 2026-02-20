package com.example.lms_course_service.repository;

import com.example.lms_course_service.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

//    List<Course> findByCreatedBy(User teacher);
//
//    //List<CourseResponseDTO> getAllCourses();
//
//
//   // List<CourseStudent> findByStudentId(Long studentId);
   Optional<Course> findByIdAndDeletedFalse(Long id);
    List<Course> findByDeletedFalse();
    List<Course> findByTeacherIdAndDeletedFalse(Long teacherId);
    long countByTeacherIdAndDeletedFalse(Long teacherId);
   // boolean existsByCourseIdAndStudentId(Long courseId, Long studentId);

    //long countByStudentId(Long studentId);
}
