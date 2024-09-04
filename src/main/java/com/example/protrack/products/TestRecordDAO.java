package com.example.protrack.products;

import com.example.protrack.databaseutil.DatabaseConnection;
import com.example.protrack.users.AbstractUser;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class TestRecordDAO {
    private Connection connection;

    public TestRecordDAO() {
        connection = DatabaseConnection.getInstance();
    }

    public void createTable() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS testRecords ("
                            + "testRecordId INTEGER PRIMARY KEY, "
                            + "productId INTEGER NOT NULL, "
                            + "stepsId INTEGER NOT NULL "
                            + ")"
            );

        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void newTestRecord(TestRecords testRecords) {
        try {
            PreparedStatement insertAccount = connection.prepareStatement(
                    "INSERT INTO testRecords (testRecordId, productId, stepsId) VALUES (?, ?, ?)"
            );

            insertAccount.setInt(1, testRecords.getTestRecordId());
            insertAccount.setInt(2, testRecords.getProductId());
            insertAccount.setInt(3, testRecords.getStepsId());

            insertAccount.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
}
