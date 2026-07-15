package com.example.studentprofilemanager.database;

import java.sql.Connection;

public class DatabaseConnection {

    private static Connection connection;

    private DatabaseConnection() {
    }

    public static Connection getConnection() {

        // Database connection will be implemented later.

        return connection;
    }
}