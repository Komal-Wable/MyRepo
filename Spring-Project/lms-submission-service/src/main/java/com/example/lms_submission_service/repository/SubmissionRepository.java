package com.example.lms_submission_service.repository;

import com.example.lms_submission_service.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository
        extends JpaRepository<Submission, Long> {

    boolean existsByAssignmentIdAndStudentIdAndDeletedFalse(
            Long assignmentId,
            Long studentId
    );

    List<Submission> findByAssignmentIdAndDeletedFalse(
            Long assignmentId);
    List<Submission> findByAssignmentId(Long assignmentId);
    ///List<Submission> findByAssignmentIdAndDeletedFalse(Long assignmentId);

    Optional<Submission>
    findByAssignmentIdAndStudentIdAndDeletedFalse(
            Long assignmentId,
            Long studentId);



    @Query("""
        SELECT COUNT(DISTINCT s.studentId)
        FROM Submission s
        WHERE s.teacherId = :teacherId
    """)
    long countDistinctStudents(
            @Param("teacherId") Long teacherId);



    @Query("""
SELECT DATE(s.submittedAt), COUNT(s)
FROM Submission s
WHERE s.teacherId = :teacherId
AND s.deleted = false
GROUP BY DATE(s.submittedAt)
ORDER BY DATE(s.submittedAt)
""")
    List<Object[]> getSubmissionTrend(Long teacherId);



    @Query("""
SELECT COUNT(s)
FROM Submission s
WHERE s.studentId = :studentId
AND s.deleted = false
""")
    long countStudentSubmissions(Long studentId);





    @Query("""
SELECT DATE(s.submittedAt), COUNT(s)
FROM Submission s
WHERE s.studentId = :studentId
GROUP BY DATE(s.submittedAt)
ORDER BY DATE(s.submittedAt)
""")
    List<Object[]> submissionTrend(Long studentId);



    @Query("""
SELECT s.assignmentId
FROM Submission s
WHERE s.studentId = :studentId
AND s.deleted = false
""")
    List<Long> findAssignmentIdsByStudentId(Long studentId);

}
