package com.example.project_prm.Models;

public class RegisterResult {
    private final boolean success;
    private final String message;
    private final int Id;

    public RegisterResult(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.Id = -1;
    }

    public RegisterResult(boolean success, String message, int Id) {
        this.success = success;
        this.message = message;
        this.Id = Id;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return Id;
    }
}