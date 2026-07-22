package com.example.studentprofilemanager.service;

import com.example.studentprofilemanager.model.Session;
import com.example.studentprofilemanager.model.Student;

/**
 * Factory Method pattern: centralizes the two different ways a Session
 * is constructed, depending on which role just logged in.
 *
 * Admin sessions have no backing Administrator object today (authenticate()
 * only returns a boolean), so userId is a placeholder (-1) and fullName is
 * unavailable — this factory documents that choice in one place instead of
 * it being an unexplained magic number inline in LoginController.
 */
public final class SessionFactory {

    private SessionFactory() {
    }

    public static Session createAdminSession(String username) {
        return new Session(-1, username, null, "ADMIN");
    }

    public static Session createStudentSession(Student student) {
        return new Session(
                student.getUserId(),
                student.getUsername(),
                student.getFullName(),
                "STUDENT"
        );
    }
}