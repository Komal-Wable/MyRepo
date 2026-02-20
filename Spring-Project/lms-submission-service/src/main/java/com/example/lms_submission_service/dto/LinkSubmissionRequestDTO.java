package com.example.lms_submission_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinkSubmissionRequestDTO {
    private Long assignmentId;

    @JsonProperty("linkUrl")
    private String linkUrl;

}
