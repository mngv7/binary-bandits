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

    /**
     * Retrieves the single connection instance. If the connection is not
     * established or is invalid, a new connection will be created.
     *
     * @return the established database connection
     */
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

    /**
     * Checks if the current database connection is valid.
     *
     * @return true if the connection is valid, false otherwise
     */
    private static boolean isConnectionValid() {
        try {
            return connection != null && connection.isValid(2); // 2-second timeout
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Closes the database connection if it is currently established.
     * Allows for reinitialization of the connection.
     */
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
