package com.example.project_prm.Models;

import com.example.project_prm.Entities.User;

public class GoogleLoginResult {
    private final boolean success;
    private final String message;
    private final User user;

    public GoogleLoginResult(boolean success, String message, User user) {
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
