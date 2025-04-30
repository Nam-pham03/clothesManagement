package com.example.project_prm.Models;

public class ResetPasswordResult {
    private final boolean success;
    private final String message;
    private final int userId;

    public ResetPasswordResult(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.userId = -1;
    }

    public ResetPasswordResult(boolean success, String message, int userId) {
        this.success = success;
        this.message = message;
        this.userId = userId;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getUserId() {
        return userId;
    }
}