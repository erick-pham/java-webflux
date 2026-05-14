package com.example.erick.shared.context;

public class UserRequestDTO {
    private String userId;
    private String username;

    // Constructor, getters, setters
    public UserRequestDTO(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
