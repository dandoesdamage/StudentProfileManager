package com.example.studentprofilemanager.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Central place for opening JDBC connections to the
 * student_profile_manager MySQL database (XAMPP, default credentials).
 *
 * <p>Repositories call {@link #getConnection()} to obtain a fresh
 * connection per operation. Nothing else in the application should
 * hold JDBC connection details directly.</p>
 */
public final class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/student_profile_manager?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}