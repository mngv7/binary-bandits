package com.example.protrack.products;

import com.example.protrack.databaseutil.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class StepsDAO {
    private Connection connection;

    public StepsDAO() {
        connection = DatabaseConnection.getInstance();
    }

    public void createTables() {
        try {
            Statement createTable = connection.createStatement();

            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS steps ("
                            + "stepsId INTEGER PRIMARY KEY, "
                            + "partsId VARCHAR NOT NULL, "
                            + "stepDescription VARCHAR NOT NULL, "
                            + "checkType VARCHAR NOT NULL, "
                            + "checkCriteria VARCHAR NOT NULL,"
                            + ")"
            );

        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
}
