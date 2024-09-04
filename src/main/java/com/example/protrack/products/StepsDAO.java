package com.example.protrack.products;

import com.example.protrack.databaseutil.DatabaseConnection;
import com.example.protrack.users.AbstractUser;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


public class StepsDAO {
    private Connection connection;

    public StepsDAO() {
        connection = DatabaseConnection.getInstance();
    }

    public void createTable() {
        try {
            Statement createTable = connection.createStatement();

            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS steps ("
                            + "stepsId INTEGER PRIMARY KEY, "
                            + "partsId VARCHAR NOT NULL, "
                            + "stepNum INTEGER NOT NULL, "
                            + "stepDescription VARCHAR NOT NULL, "
                            + "checkType VARCHAR NOT NULL, "
                            + "checkCriteria VARCHAR NOT NULL"
                            + ")"
            );

        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void newSteps(Steps steps) {
        try {
            PreparedStatement insertAccount = connection.prepareStatement(
                    "INSERT INTO steps (stepsId, partsId, stepNum, stepDescription, checkType, checkCriteria) VALUES (?, ?, ?, ?, ?, ?)"
            );

            insertAccount.setInt(1, steps.getStepsId());
            insertAccount.setString(2, steps.getPartsId());
            insertAccount.setInt(3, steps.getStepNum());
            insertAccount.setString(4, steps.getStepDescription());
            insertAccount.setString(5, steps.getCheckType());
            insertAccount.setString(6, steps.getCheckCriteria());

            insertAccount.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
}
