package com.example.protrack.timesheets;

import com.example.protrack.utility.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TimesheetsDAO {
    private final Connection connection;

    public TimesheetsDAO() { connection = DatabaseConnection.getInstance(); }

    /**
     * Creates timesheets table
     */
    public void createTable() {
        try {
            // Create a statement object for sending SQL queries to the database
            Statement createTable = connection.createStatement();

            // Execute the SQL query to create a table named 'timesheets' if it does not already exist
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS timesheets (" +
                            "startTime DATETIME NOT NULL, " +
                            "endTime DATETIME NOT NULL, " +
                            "employeeId INTEGER NOT NULL, " +
                            "productOrderId INTEGER NOT NULL," +
                            "PRIMARY KEY (employeeId, productOrderId)" +
                            ")"
            );

        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that may occur during table creation
            System.err.println(ex);
        }
    }
}
