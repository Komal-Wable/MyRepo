package com.example.lms_assignment_service.repository;

import com.example.lms_assignment_service.entity.Assignment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository
        extends JpaRepository<Assignment, Long> {
    Optional<Assignment> findByIdAndDeletedFalse(Long id);
    List<Assignment> findByCourseIdAndDeletedFalse(Long courseId);


    @Transactional
    @Modifying
    @Query("""
UPDATE Assignment a
SET a.deleted = true
WHERE a.courseId = :courseId
""")
    void softDeleteByCourseId(Long courseId);

    long countByTeacherId(Long teacherId);

//    @Query("""
//SELECT COUNT(a)
//FROM Assignment a
//WHERE a.deleted = false
//AND a.courseId IN (
//
//    SELECT cs.courseId
//    FROM CourseEnrollment cs
//    WHERE cs.studentId = :studentId
//)
//AND a.id NOT IN (
//
//    SELECT s.assignmentId
//    FROM Submission s
//    WHERE s.studentId = :studentId
//    AND s.deleted = false
//)
//""")
//    long countPendingAssignments(Long studentId);

    @Query("""
SELECT COUNT(a)
FROM Assignment a
WHERE a.deleted = false
AND a.courseId IN :courseIds
""")
    long countAssignments(List<Long> courseIds);





    List<Assignment> findByCourseIdInAndDeletedFalse(List<Long> courseIds);

}
