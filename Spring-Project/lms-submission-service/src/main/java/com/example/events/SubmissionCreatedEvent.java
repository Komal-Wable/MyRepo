package com.example.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubmissionCreatedEvent {

    private Long submissionId;
    private Long assignmentId;
    private Long studentId;
    private Long teacherId;
}
