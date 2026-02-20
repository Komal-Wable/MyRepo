package com.example.lms_submission_service.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SQLDelete(sql =
        "UPDATE submission SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long assignmentId;
    private Long teacherId;
    private Long studentId;
    @Enumerated(EnumType.STRING)
    private SubmissionType  submissionType;


    @Column(name = "link_url")
    private String linkUrl;
    private String fileName;

    private String originalFileName;

    private LocalDateTime submittedAt;
    @Column(nullable = false)
    private boolean deleted = false;

}
