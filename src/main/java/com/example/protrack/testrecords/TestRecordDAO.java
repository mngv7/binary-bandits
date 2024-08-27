package com.example.protrack.testrecords;

import com.example.protrack.databaseutil.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class TestRecordDAO {
    private Connection connection;

    public TestRecordDAO() {
        connection = DatabaseConnection.getInstance();
    }

    public void createTables() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS testRecordParts ("
                            + "productId INTEGER PRIMARY KEY, "
                            + "partId VARCHAR NOT NULL, "
                            + "partQuantity VARCHAR NOT NULL, "
                            + ")"
            );
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS testRecordSteps ("
                            + "productId INTEGER PRIMARY KEY, "
                            + "stepNum VARCHAR NOT NULL, "
                            + "stepDescription VARCHAR NOT NULL, "
                            + "checkType VARCHAR NOT NULL, "
                            + ")"
            );

        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
}
