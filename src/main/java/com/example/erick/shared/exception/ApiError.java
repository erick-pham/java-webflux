package com.example.erick.shared.exception;

public record ApiError(
        String code,
        String message) {
}