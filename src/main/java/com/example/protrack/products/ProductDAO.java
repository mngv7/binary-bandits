package com.example.protrack.products;

import com.example.protrack.databaseutil.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductDAO {
    private Connection connection;

    public ProductDAO() {
        connection = DatabaseConnection.getInstance();
    }

    public void createTable() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS testRecords ("
                            + "productId INTEGER PRIMARY KEY, "
                            + "name VARCHAR NOT NULL, "
                            + "dateCreated DATE NOT NULL, "
                            + "employeeId INTEGER NOT NULL, "
                            + "reqPartsId INTEGER NOT NULL, "
                            + "PIId INTEGER NOT NULL, "
                            + "status VARCHAR NOT NULL"
                            + ")"
            );


        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
}
