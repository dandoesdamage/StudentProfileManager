package com.example.studentprofilemanager.service;

import com.example.studentprofilemanager.model.Session;
import com.example.studentprofilemanager.model.Student;

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