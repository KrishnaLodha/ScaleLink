package com.scalink.exception;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

public class ErrorResponse {

    private Instant timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private Map<String, String> validationErrors;
    public Instant getTimestamp() { return this.timestamp; }
    public int getStatus() { return this.status; }
    public String getError() { return this.error; }
    public String getMessage() { return this.message; }
    public String getPath() { return this.path; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public void setStatus(int status) { this.status = status; }
    public void setError(String error) { this.error = error; }
    public void setMessage(String message) { this.message = message; }
    public void setPath(String path) { this.path = path; }
    public java.util.Map<String, String> getValidationErrors() { return this.validationErrors; }
    public void setValidationErrors(java.util.Map<String, String> validationErrors) { this.validationErrors = validationErrors; }

    public ErrorResponse() {}
    public ErrorResponse(Instant timestamp, int status, String error, String message, String path) { this.timestamp = timestamp; this.status = status; this.error = error; this.message = message; this.path = path; }
    public static ErrorResponseBuilder builder() { return new ErrorResponseBuilder(); }
    public static class ErrorResponseBuilder {
        private Instant timestamp;
        public ErrorResponseBuilder timestamp(Instant timestamp) { this.timestamp = timestamp; return this; }
        private int status;
        public ErrorResponseBuilder status(int status) { this.status = status; return this; }
        private String error;
        public ErrorResponseBuilder error(String error) { this.error = error; return this; }
        private String message;
        public ErrorResponseBuilder message(String message) { this.message = message; return this; }
        private String path;
        public ErrorResponseBuilder path(String path) { this.path = path; return this; }
        private java.util.Map<String, String> validationErrors;
        public ErrorResponseBuilder validationErrors(java.util.Map<String, String> validationErrors) { this.validationErrors = validationErrors; return this; }
        public ErrorResponse build() { ErrorResponse e = new ErrorResponse(timestamp, status, error, message, path); e.setValidationErrors(validationErrors); return e; }
    }
}
