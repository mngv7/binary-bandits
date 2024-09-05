package com.example.protrack.products;

import com.example.protrack.databaseutil.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class TestRecordStepsDAO {
    private Connection connection;

    public TestRecordStepsDAO() {
        connection = DatabaseConnection.getInstance();
    }

    public void createTable() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS testRecordSteps ("
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
            System.err.println(ex);
        }
    }

    public void newTestRecordStep(TestRecordSteps testRecordStep) {
        try {
            PreparedStatement insertStep = connection.prepareStatement(
                    "INSERT INTO testRecordSteps (stepId, productId, stepNumber, stepDescription, stepCheckType, stepCheckCriteria) "
                            + "VALUES (?, ?, ?, ?, ?, ?)"
            );

            insertStep.setInt(1, testRecordStep.getStepId());
            insertStep.setInt(2, testRecordStep.getProductId());
            insertStep.setInt(3, testRecordStep.getStepNumber());
            insertStep.setString(4, testRecordStep.getStepDescription());
            insertStep.setString(5, testRecordStep.getStepCheckType());
            insertStep.setString(6, testRecordStep.getStepCheckCriteria());

            insertStep.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
}
