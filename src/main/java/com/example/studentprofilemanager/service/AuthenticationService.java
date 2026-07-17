package com.example.studentprofilemanager.service;

import com.example.studentprofilemanager.db.DatabaseConnection;
import com.example.studentprofilemanager.model.Student;
import com.example.studentprofilemanager.repository.StudentRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticationService {

    public boolean authenticate(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        String sql = "SELECT password FROM users WHERE username = ? AND role = 'ADMIN'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    return password.equals(storedPassword);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database connection error during admin authentication.", e);
        }

        return false;
    }

    /* ---------------------------------------------------------------
       Unchanged: student authentication, already JDBC-backed since
       StudentRepository was migrated in Phase 3.
       --------------------------------------------------------------- */
    public Student authenticateStudent(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        Student student = StudentRepository.getInstance().findStudentByUsername(username);
        if (student != null && password.equals(student.getPassword())) {
            return student;
        }
        return null;
    }

}