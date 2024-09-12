package com.example.protrack.products;

import com.example.protrack.databaseutil.DatabaseConnection;

import java.sql.*;

public class BillOfMaterialsDAO {
    private final Connection connection;

    public BillOfMaterialsDAO() {
        connection = DatabaseConnection.getInstance();
    }

    // Create table with composite primary key
    public void createTable() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS requiredParts ("
                            + "partsId INTEGER NOT NULL, "
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
    public void newRequiredParts(BillOfMaterials billOfMaterials) {
        try {
            PreparedStatement insertRequiredParts = connection.prepareStatement(
                    "INSERT INTO requiredParts (partsId, productId, requiredAmount) VALUES (?, ?, ?)"
            );

            insertRequiredParts.setInt(1, billOfMaterials.getPartsId());
            insertRequiredParts.setInt(2, billOfMaterials.getProductId());
            insertRequiredParts.setInt(3, billOfMaterials.getRequiredAmount());

            insertRequiredParts.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public boolean isTableEmpty() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM requiredParts");
            rs.next();
            int count = rs.getInt("rowcount");
            rs.close();
            return count == 0;
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return false;
    }
}
