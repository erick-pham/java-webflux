package com.example.erick.shared.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    USER_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "User not found"),

    INVALID_REQUEST(
            HttpStatus.BAD_REQUEST,
            "Invalid request"),

    INTERNAL_SERVER_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Internal server error");

    private final HttpStatus status;
    private final String defaultMessage;

    ErrorCode(
            HttpStatus status,
            String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}