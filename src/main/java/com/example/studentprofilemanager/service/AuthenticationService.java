package com.example.studentprofilemanager.service;

import com.example.studentprofilemanager.model.Student;

public class AuthenticationService {

    private final AuthenticationStrategy adminStrategy = new AdminAuthenticationStrategy();
    private final AuthenticationStrategy studentStrategy = new StudentAuthenticationStrategy();

    public boolean authenticate(String username, String password) {
        return adminStrategy.authenticate(username, password).isSuccess();
    }

    public Student authenticateStudent(String username, String password) {
        AuthResult result = studentStrategy.authenticate(username, password);
        return result.isSuccess() ? result.getStudent() : null;
    }

}