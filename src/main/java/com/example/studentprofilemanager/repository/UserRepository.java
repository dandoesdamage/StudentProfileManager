package com.example.studentprofilemanager.repository;

import com.example.studentprofilemanager.db.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * JDBC-backed read access for the `users` table. Currently limited to
 * what the dashboard needs (a total user count); grow this class only
 * as further user-management features require it.
 */
public class UserRepository {

    private static UserRepository instance;

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    /**
     * Count of all registered users (both ADMIN and STUDENT roles).
     * Returns 0 if the table is empty.
     */
    public int countUsers() {
        String sql = "SELECT COUNT(*) AS total FROM users";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to count users.", e);
        }

        return 0;
    }
}