package com.example.lms_assignment_service.dto;

import com.example.lms_assignment_service.entity.AssignmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

//import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class AssignmentListDTO {

    private Long id;
    private String title;
    private String description;
    private AssignmentType assignmentType;
    private LocalDateTime dueDate;

    private String instructionFileName;
    private String instructionOriginalName;
    private String instructionPathLink;
}
