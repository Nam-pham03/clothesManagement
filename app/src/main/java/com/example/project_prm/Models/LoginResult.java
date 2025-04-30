package com.example.project_prm.Models;

import com.example.project_prm.Entities.User;

public class LoginResult {
    private final boolean success;
    private final String message;
    private final User user;

    public LoginResult(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.user = null;
    }

    public LoginResult(boolean success, String message, User user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}