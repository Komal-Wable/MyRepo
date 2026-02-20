package com.example.lms_course_service.repository;

import com.example.lms_course_service.entity.CourseEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseEnrollmentRepository
        extends JpaRepository<CourseEnrollment, Long> {

    @Query("""
SELECT cs.courseId
FROM CourseEnrollment cs
WHERE cs.studentId = :studentId
""")
    List<Long> findCourseIdsByStudentId(Long studentId);

    long countByStudentId(Long studentId);
    boolean existsByCourseIdAndStudentId(Long courseId, Long studentId);
}
