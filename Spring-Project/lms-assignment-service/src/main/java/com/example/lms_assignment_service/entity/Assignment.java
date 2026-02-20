package com.example.lms_assignment_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

//import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "assignments")
@Getter
@Setter
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private AssignmentType assignmentType;

    private String instructionPathLink;

    private String instructionOriginalName;
    private String instructionFileName;

    private LocalDateTime dueDate;


    private Long courseId;
    private Long teacherId;

    private LocalDateTime createdAt;

    private boolean deleted = false;
}
