package com.example.lms_evaluation_service.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {

    private String error;
    private String message;
    private LocalDateTime timestamp;
    private int status;

    public ErrorResponse(String error, String message,
                         LocalDateTime timestamp, int status) {
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
        this.status = status;
    }



    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
