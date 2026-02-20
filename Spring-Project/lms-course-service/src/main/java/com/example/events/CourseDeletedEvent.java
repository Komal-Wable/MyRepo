package com.example.events;

public class CourseDeletedEvent {

    private Long courseId;

    public CourseDeletedEvent() {}

    public CourseDeletedEvent(Long courseId) {
        this.courseId = courseId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
