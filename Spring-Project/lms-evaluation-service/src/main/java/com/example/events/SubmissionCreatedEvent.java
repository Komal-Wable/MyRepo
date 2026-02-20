package com.example.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionCreatedEvent {

    private Long submissionId;
    private Long assignmentId;
    private Long studentId;
    private Long teacherId;
}
