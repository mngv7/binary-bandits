package com.example.protrack.products;

import com.example.protrack.utility.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<TestRecord> getAllTRFromProductID(int productIdInput) {
        // empty list of products
        List<TestRecord> testRecords = new ArrayList<>();

        try {

            PreparedStatement getTestRecord = connection.prepareStatement(
                    "SELECT * " +
                        "FROM testRecord a " +
                        "WHERE a.productId = ?");
            getTestRecord.setInt(1, productIdInput);
            ResultSet rs = getTestRecord.executeQuery();


//            createTable.execute(
//                    "CREATE TABLE IF NOT EXISTS testRecord ("
//                            + "stepId INTEGER NOT NULL, "
//                            + "productId INTEGER NOT NULL, "
//                            + "stepNumber INTEGER NOT NULL, "
//                            + "stepDescription VARCHAR NOT NULL, "
//                            + "stepCheckType VARCHAR NOT NULL, "
//                            + "stepCheckCriteria VARCHAR NOT NULL, "
//                            + "PRIMARY KEY (stepId, productId)"
//                            + ")"
//            );

            // while there is a row, get those values and add it to the list
            while (rs.next()) {
                int stepId = rs.getInt("stepId");
                int productId = rs.getInt("productId");
                int stepNum = rs.getInt("stepNumber");
                String stepDescription = rs.getString("stepDescription");
                String stepCheckType = rs.getString("stepCheckType");
                String stepCheckCriteria = rs.getString("stepCheckCriteria");

                TestRecord testRecord = new TestRecord(stepId, productId, stepNum, stepDescription, stepCheckType, stepCheckCriteria);
                testRecords.add(testRecord);
            }
        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that may occur during table creation
            System.err.println(ex);
        }
        // return list of products in table
        return testRecords;
    }
}
