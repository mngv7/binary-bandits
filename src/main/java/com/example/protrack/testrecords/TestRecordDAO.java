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
                    "CREATE TABLE IF NOT EXISTS testRecords ("
                            + "testRecordId INTEGER PRIMARY KEY, "
                            + "productName VARCHAR NOT NULL, "
                            + "dateCreated VARCHAR NOT NULL, "
                            + "employeeId INTEGER NOT NULL, "
                            + "reqPartsId INTEGER NOT NULL, "
                            + "stepsId INTEGER NOT NULL, "
                            + "PIId INTEGER NOT NULL, "
                            + ")"
            );


        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
}
