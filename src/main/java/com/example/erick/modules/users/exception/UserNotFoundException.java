package com.example.erick.modules.users.exception;

import org.springframework.web.server.ResponseStatusException;

public class UserNotFoundException extends ResponseStatusException {

    public UserNotFoundException(Long userId) {
        super(org.springframework.http.HttpStatus.NOT_FOUND, "User not found with id: " + userId);
    }
}
