package com.example.studentprofilemanager.service;

import com.example.studentprofilemanager.model.Student;

public final class AuthResult {

    private final boolean success;
    private final Student student; // null for admin logins

    private AuthResult(boolean success, Student student) {
        this.success = success;
        this.student = student;
    }

    public static AuthResult success() {
        return new AuthResult(true, null);
    }

    public static AuthResult success(Student student) {
        return new AuthResult(true, student);
    }

    public static AuthResult failure() {
        return new AuthResult(false, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public Student getStudent() {
        return student;
    }
}