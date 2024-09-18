package com.example.protrack.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Single connection instance
    private static Connection connection;

    // Private constructor to prevent instantiation
    private DatabaseConnection() {
    }

    // Get the single connection instance
    public static Connection getInstance() {
        if (connection == null || !isConnectionValid()) {
            try {
                // Initialize the database connection
                connection = DriverManager.getConnection("jdbc:sqlite:database.db");
                System.out.println("Database connection established.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    // Check if the connection is valid
    private static boolean isConnectionValid() {
        try {
            return connection != null && connection.isValid(2); // 2-second timeout
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Close the database connection
    public static void close() {
        if (connection != null) {
            try {
                connection.close();
                connection = null; // Allow reinitialization
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}