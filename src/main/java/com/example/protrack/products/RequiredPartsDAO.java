package com.example.protrack.products;

import com.example.protrack.databaseutil.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class RequiredPartsDAO {
    private Connection connection;

    public RequiredPartsDAO() {
        connection = DatabaseConnection.getInstance();
    }

    public void createTables() {
        try {
            Statement createTable = connection.createStatement();

            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS requiredParts ("
                            + "reqPartsId INTEGER PRIMARY KEY, "
                            + "partsId VARCHAR NOT NULL, "
                            + "requiredAmt INTEGER NOT NULL, "
                            + "currentAmt INTEGER NOT NULL, "
                            + ")"
            );


        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
}
