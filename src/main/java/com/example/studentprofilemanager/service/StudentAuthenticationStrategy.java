package com.example.studentprofilemanager.service;

import com.example.studentprofilemanager.model.Student;
import com.example.studentprofilemanager.repository.StudentRepository;

public class StudentAuthenticationStrategy implements AuthenticationStrategy {

    @Override
    public AuthResult authenticate(String username, String password) {
        if (username == null || password == null) {
            return AuthResult.failure();
        }

        Student student = StudentRepository.getInstance().findStudentByUsername(username);

        if (student != null && password.equals(student.getPassword())) {
            return AuthResult.success(student);
        }

        return AuthResult.failure();
    }
}