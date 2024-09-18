package com.example.protrack.products;

import com.example.protrack.utility.DatabaseConnection;

import java.sql.*;

public class TestRecordDAO {
    private final Connection connection;

    public TestRecordDAO() {
        connection = DatabaseConnection.getInstance();
    }

    /**
     * Creates test record table
     */
    public void createTable() {
        try {
            // Create a statement object for sending SQL queries to the database
            Statement createTable = connection.createStatement();

            // Execute the SQL query to create a table named 'testRecord' if it does not already exist
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS testRecord ("
                            + "stepId INTEGER NOT NULL, "
                            + "productId INTEGER NOT NULL, "
                            + "stepNumber INTEGER NOT NULL, "
                            + "stepDescription VARCHAR NOT NULL, "
                            + "stepCheckType VARCHAR NOT NULL, "
                            + "stepCheckCriteria VARCHAR NOT NULL, "
                            + "PRIMARY KEY (stepId, productId)"
                            + ")"
            );
        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that may occur during table creation
            System.err.println(ex);
        }
    }

    /**
     * Inserts inputted test record step into test record table
     * @param testRecordStep a step in a step record
     */
    public void newTestRecordStep(TestRecord testRecordStep) {
        try {
            PreparedStatement insertStep = connection.prepareStatement(
                    "INSERT INTO testRecord (stepId, productId, stepNumber, stepDescription, stepCheckType, stepCheckCriteria) "
                            + "VALUES (?, ?, ?, ?, ?, ?)"
            );

            // sets test record value into statement
            insertStep.setInt(1, testRecordStep.getStepId());
            insertStep.setInt(2, testRecordStep.getProductId());
            insertStep.setInt(3, testRecordStep.getStepNumber());
            insertStep.setString(4, testRecordStep.getStepDescription());
            insertStep.setString(5, testRecordStep.getStepCheckType());
            insertStep.setString(6, testRecordStep.getStepCheckCriteria());

            // executes statement and enters test record values into test record table
            insertStep.execute();
        } catch (SQLException ex) {
            // print error if error occurs
            System.err.println(ex);
        }
    }

    /**
     * Checks if table is empty.
     * @return true if empty, else returns false.
     */
    public boolean isTableEmpty() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM testRecord");
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
