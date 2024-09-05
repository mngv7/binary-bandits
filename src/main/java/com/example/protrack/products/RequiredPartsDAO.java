package com.example.protrack.products;

import com.example.protrack.databaseutil.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class RequiredPartsDAO {
    private Connection connection;

    public RequiredPartsDAO() {
        connection = DatabaseConnection.getInstance();
    }

    // Create table with composite primary key
    public void createTable() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS requiredParts ("
                            + "partsId VARCHAR NOT NULL, "
                            + "productId INTEGER NOT NULL, "
                            + "requiredAmount INTEGER NOT NULL, "
                            + "PRIMARY KEY (partsId, productId)"
                            + ")"
            );

        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    // Insert new required parts entry into the table
    public void newRequiredParts(RequiredParts requiredParts) {
        try {
            PreparedStatement insertRequiredParts = connection.prepareStatement(
                    "INSERT INTO requiredParts (partsId, productId, requiredAmount) VALUES (?, ?, ?)"
            );

            insertRequiredParts.setString(1, requiredParts.getPartsId());
            insertRequiredParts.setInt(2, requiredParts.getProductId());
            insertRequiredParts.setInt(3, requiredParts.getRequiredAmount());

            insertRequiredParts.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
}
