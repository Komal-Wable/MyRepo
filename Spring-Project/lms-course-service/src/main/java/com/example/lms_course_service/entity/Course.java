package com.example.lms_course_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "courses")
@Getter
@Setter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Column(nullable = false)
    private boolean deleted = false;


//    @ManyToOne
//    @JoinColumn(name = "created_by")
//    private User createdBy;
//
//
//    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
//    private List<Assignment> assignments;

    private Long teacherId;
//    @OneToMany(mappedBy = "course")
//    private List<CourseStudent> students;


}