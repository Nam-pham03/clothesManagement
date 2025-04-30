package com.example.project_prm.Models;

public class ProfileResult {
    private final boolean success;
    private final String message;

    public ProfileResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}