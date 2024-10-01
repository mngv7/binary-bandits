package com.example.protrack.timesheets;

import com.example.protrack.utility.DatabaseConnection;

import java.sql.*;

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

    /**
     * Inserts inputted timesheet into timesheet table
     */
    public void newTimesheet(Timesheets timesheets) {
        try {
            PreparedStatement insertStep = connection.prepareStatement(
                    "INSERT INTO timesheets (startTime, endTime, employeeID, productOrderID) " +
                            "VALUES (?, ?, ?, ?)"
            );

            // Sets timesheet value into statement
            insertStep.setObject(1, timesheets.getStartTime());
            insertStep.setObject(2, timesheets.getEndTime());
            insertStep.setInt(3, timesheets.getEmployeeID());
            insertStep.setInt(4, Integer.parseInt(timesheets.getProductOrderID().toString()));

            insertStep.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isTableEmpty() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM timesheets");
            rs.next();
            int count = rs.getInt("rowcount");
            rs.close();
            return count == 0;
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return false;
    }
}
