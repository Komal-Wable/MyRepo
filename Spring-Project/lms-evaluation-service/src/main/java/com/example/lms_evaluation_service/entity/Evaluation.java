package com.example.lms_evaluation_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "evaluations")
@SQLDelete(sql =
        "UPDATE evaluations SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true, nullable = false)
    private Long submissionId;

    private Integer marks;

    private String grade;

    private String feedback;
    private Long teacherId;
    private boolean graded = false;



    private LocalDateTime gradedAt;

    private boolean deleted = false;
    private Long assignmentId;

    private Long studentId;
    @Column(name = "scorecard_file_name")
    private String scorecardFileName;

    @Column(name = "scorecard_original_name")
    private String scorecardOriginalName;

}
