package com.example.studentprofilemanager.service;

import com.example.studentprofilemanager.db.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminAuthenticationStrategy implements AuthenticationStrategy {

    @Override
    public AuthResult authenticate(String username, String password) {
        if (username == null || password == null) {
            return AuthResult.failure();
        }

        String sql = "SELECT password FROM users WHERE username = ? AND role = 'ADMIN'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    if (password.equals(storedPassword)) {
                        return AuthResult.success();
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database connection error during admin authentication.", e);
        }

        return AuthResult.failure();
    }
}