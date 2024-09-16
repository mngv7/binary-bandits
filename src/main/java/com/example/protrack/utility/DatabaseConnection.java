package com.example.protrack.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;

    private DatabaseConnection() {
        // Private constructor to prevent instantiation
    }

    public static Connection getInstance() {
        if (connection == null || !isConnectionValid()) {
            try {
                // Initialises the database connection
                connection = DriverManager.getConnection("jdbc:sqlite:database.db");
                System.out.println("Database connection established.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    private static boolean isConnectionValid() {
        try {
            return connection != null && connection.isValid(2); // Check if connection is valid with a timeout of 2 seconds
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to close the connection
    public static void close() {
        if (connection != null) {
            try {
                connection.close();
                connection = null; // Clear reference to allow reinitialization
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}