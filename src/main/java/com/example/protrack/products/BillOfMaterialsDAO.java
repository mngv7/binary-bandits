package com.example.protrack.products;

import com.example.protrack.utility.DatabaseConnection;

import java.sql.*;

public class BillOfMaterialsDAO {
    private final Connection connection;

    public BillOfMaterialsDAO() {
        connection = DatabaseConnection.getInstance();
    }

    // Create table with composite primary key
    public void createTable() {
        try {
            // Create a statement object for sending SQL queries to the database
            Statement createTable = connection.createStatement();

            // Execute the SQL query to create a table named 'requiredParts' if it does not already exist
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS requiredParts ("
                            + "partsId INTEGER NOT NULL, "
                            + "productId INTEGER NOT NULL, "
                            + "requiredAmount INTEGER NOT NULL, "
                            + "PRIMARY KEY (partsId, productId)"
                            + ")"
            );

        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that may occur during table creation
            System.err.println(ex);
        }
    }

    // Insert new required parts entry into the table
    public void newRequiredParts(BillOfMaterials billOfMaterials) {
        try {
            PreparedStatement insertRequiredParts = connection.prepareStatement(
                    "INSERT INTO requiredParts (partsId, productId, requiredAmount) VALUES (?, ?, ?)"
            );

            // sets required parts values into statement
            insertRequiredParts.setInt(1, billOfMaterials.getPartsId());
            insertRequiredParts.setInt(2, billOfMaterials.getProductId());
            insertRequiredParts.setInt(3, billOfMaterials.getRequiredAmount());

            // executes and inserts required parts values into table
            insertRequiredParts.execute();
        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that may occur during table creation
            System.err.println(ex);
        }
    }

    /**
     * Getter that gets all products of table
     * @return all products in product table
     */
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
