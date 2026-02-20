package com.example.lms_course_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "course_enrollments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"course_id", "student_id"})
        })
@Getter
@Setter
public class CourseEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    private LocalDateTime enrolledAt;
}
