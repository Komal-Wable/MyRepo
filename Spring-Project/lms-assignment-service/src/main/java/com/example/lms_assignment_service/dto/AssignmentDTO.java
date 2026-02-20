package com.example.lms_assignment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentDTO {

    private Long id;
    private String title;
    private Long courseId;
    private LocalDateTime dueDate;
    private  Long teacherId;
    private String assignmentType;


//    public AssignmentDTO(Long id, String title, Long courseId) {
//    }
}
