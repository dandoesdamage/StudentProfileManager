package com.example.studentprofilemanager.model;

import java.io.Serializable;

/**
 * Minimal, serializable snapshot of the currently logged-in user.
 * This is intentionally NOT a full User/Student/Administrator object —
 * it holds only what's needed to identify the user and their role
 * while navigating the application.
 */
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;

    private int userId;
    private String username;
    private String fullName;
    private String role; // "ADMIN" or "STUDENT"

    public Session() {
    }

    public Session(int userId, String username, String fullName, String role) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return role;
    }

    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }

    public boolean isStudent() {
        return "STUDENT".equals(role);
    }
}