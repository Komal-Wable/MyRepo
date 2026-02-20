package com.example.events;

public class AssignmentDeletedEvent {

    private Long assignmentId;

    public AssignmentDeletedEvent() {}

    public AssignmentDeletedEvent(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }
}
