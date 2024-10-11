package com.example.protrack.parts;

import com.example.protrack.utility.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for create new parts in the database.
 * handles CRUD operations for parts.
 */
public class PartsDAO {
    private final Connection connection;

    /**
     * Constructs a PartsDAO and establishes a database connection.
     */
    public PartsDAO() {
        connection = DatabaseConnection.getInstance();
    }

    /**
     * Creates the 'parts' table if it does not already exist
     */
    public void createTable() {
        try {
            // Create a statement object for sending SQL queries to the database
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS parts ("
                            + "partsId INTEGER PRIMARY KEY, "
                            + "name VARCHAR NOT NULL, "
                            + "description VARCHAR NOT NULL, "
                            + "supplierId INTEGER NOT NULL, "
                            + "cost DOUBLE NOT NULL"
                            + ")"
            );
        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that may occur during table creation
            System.err.println(ex);
        }
    }

    /**
     * Adds a new part to the 'parts' table
     *
     * @param parts the part to be added to the table
     */
    public void newPart(Parts parts) {
        try {
            // Prepare SQL statement for inserting a new part
            PreparedStatement insertPart = connection.prepareStatement(
                    "INSERT INTO parts (partsId, name, description, supplierId, cost) VALUES (?, ?, ?, ?, ?)"
            );

            // Set values for the part fields in the SQL statement
            insertPart.setInt(1, parts.getPartsId());
            insertPart.setString(2, parts.getName());
            insertPart.setString(3, parts.getDescription());
            insertPart.setInt(4, parts.getSupplierId());
            insertPart.setDouble(5, parts.getCost());

            // Execute the insert operation
            insertPart.execute();
        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that may occur during insertion
            System.err.println(ex);
        }
    }

    /**
     * Retrieves all parts from the 'parts' table
     *
     * @return a list of all parts in the table
     */
    public List<Parts> getAllParts() {
        List<Parts> parts = new ArrayList<>();  // Initialize an empty list to hold parts

        String query = "SELECT * FROM parts";  // SQL query to select all parts

        // Try to execute the query and process the result set
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Iterate through the result set and create Parts objects
            while (rs.next()) {
                int partsId = rs.getInt("partsId");
                String name = rs.getString("name");
                String description = rs.getString("description");
                int supplierId = rs.getInt("supplierId");
                Double cost = rs.getDouble("cost");

                Parts partsItem = new Parts(partsId, name, description, supplierId, cost);
                parts.add(partsItem);  // Add each part to the list
            }
        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that may occur during retrieval
            System.err.println(ex);
        }

        // Return the list of parts
        return parts;
    }

    /**
     * Retrieves parts with a specific ID from the 'parts' table
     * @return a list of all parts with specified ID in the table
     */
    public Parts getPartById(Integer partId) {
        String query = "SELECT * FROM parts WHERE partsId = ?";

        Parts part = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, partId);

            ResultSet resultSet = preparedStatement.executeQuery();

            int partsId = resultSet.getInt("partsId");
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            int supplierId = resultSet.getInt("supplierId");
            Double cost = resultSet.getDouble("cost");

            part = new Parts(partsId, name, description, supplierId, cost);

        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return part;
    }

    /**
     * Checks if the 'parts' table is empty
     *
     * @return true if the table is empty, false otherwise
     */
    public boolean isTableEmpty() {
        try {
            // Create a statement and execute a count query on the parts table
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM parts");
            rs.next();  // Move to the first row of the result set
            int count = rs.getInt("rowcount");  // Get the count
            rs.close();
            return count == 0;  // Return true if count is 0, indicating the table is empty
        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that may occur during the check
            System.err.println(ex);
        }
        return false;  // Return false in case of an exception
    }
}
