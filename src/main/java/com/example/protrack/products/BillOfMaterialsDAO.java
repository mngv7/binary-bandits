package com.example.protrack.products;

import com.example.protrack.utility.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public List<BillOfMaterials> getBillOfMaterialsForProduct(int productId) {
        List<BillOfMaterials> billOfMaterialsList = new ArrayList<>();
        String query = "SELECT * FROM requiredParts WHERE productId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int partsId = rs.getInt("partsId");
                int requiredAmount = rs.getInt("requiredAmount");

                BillOfMaterials bom = new BillOfMaterials(partsId, productId, requiredAmount);
                billOfMaterialsList.add(bom);
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return billOfMaterialsList;
    }

    public HashMap<Integer, Integer> getPartsAndAmountsForProduct(int productId) {
        HashMap<Integer, Integer> partsMap = new HashMap<>(); // Create a HashMap to hold parts and their amounts
        String query = "SELECT partsId, requiredAmount FROM requiredParts WHERE productId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId); // Set the product ID in the query
            ResultSet rs = stmt.executeQuery();

            // Loop through the result set and populate the HashMap
            while (rs.next()) {
                int partsId = rs.getInt("partsId");
                int requiredAmount = rs.getInt("requiredAmount");
                partsMap.put(partsId, requiredAmount); // Add to the map
            }
        } catch (SQLException ex) {
            System.err.println("Error retrieving parts and amounts for product ID " + productId + ": " + ex.getMessage());
        }

        return partsMap; // Return the populated HashMap
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
