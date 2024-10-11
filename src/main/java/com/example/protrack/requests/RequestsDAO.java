package com.example.protrack.requests;

import com.example.protrack.applicationpages.WarehousePastRequests;
import com.example.protrack.products.Product;
import com.example.protrack.products.TestRecord;
import com.example.protrack.utility.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestsDAO {
    private final Connection connection;

    public RequestsDAO() {
        connection = DatabaseConnection.getInstance();
    }

    public void createTable() {
        try {
            // Create a statement object for sending SQL queries to the database
            Statement createTable = connection.createStatement();

            // Execute the SQL query to create a table named 'requests' if it does not already exist
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS requests ("
                            + "requestId INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "locationId INTEGER NOT NULL, "
                            + "partId INTEGER NOT NULL, "
                            + "quantity INTEGER NOT NULL"
                            + ")"
            );
        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that may occur during table creation
            System.err.println(ex);
        }
    }

    /**
     * Inserts inputted request into request table
     * @param requests a request for parts
     */
    public void newRequest(Requests requests) {
        try {
            PreparedStatement insertStep = connection.prepareStatement(
                    "INSERT INTO requests (locationId, partId, requestId, quantity) "
                            + "VALUES (?, ?, ?, ?)"
            );

            // sets test record value into statement
            insertStep.setInt(1, requests.getLocationId());
            insertStep.setInt(2, requests.getPartId());
            insertStep.setInt(3, requests.getRequestId());
            insertStep.setInt(4, requests.getQuantity());

            // executes statement and enters test record values into test record table
            insertStep.execute();
        } catch (SQLException ex) {
            // print error if error occurs
            System.err.println(ex);
        }
    }

    /**
     * Deletes requests table
     */
    public void dropTable() {
        String query = "DROP TABLE IF EXISTS requests";  // SQL statement to drop the requests table

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);    // executes SQL deletion statement
            System.out.println("Table 'requests' dropped successfully.");
        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that may occur during table creation
            System.err.println("Error dropping table 'requests': " + ex.getMessage());
        }
    }

    /**
     * Getter that gets all requests of table
     * @return all products in requests table
     */
    public List<Requests> getAllRequests() {
        // empty list of Requests
        List<Requests> requests = new ArrayList<>();

        // query being run, get all from requests
        String query = "SELECT * FROM requests";

        // try running query
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // while there is a row, get those values and add it to the list
            while (rs.next()) {
                int locationId = rs.getInt("locationId");
                int partId = rs.getInt("partId");
                int requestId = rs.getInt("requestId");
                int quantity = rs.getInt("quantity");

                Requests request = new Requests(locationId, partId, requestId, quantity);
                requests.add(request);
            }
        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that may occur during table creation
            System.err.println(ex);
        }
        // return list of requests in table
        return requests;
    }


    /**
     * Getter that gets PartRequests of table
     * @return PartRequests in requests table
     */
    //getPartRequests
    public List<WarehousePastRequests> getPartRequests() {
        List<WarehousePastRequests> partRequests = new ArrayList<>();

        String query = "SELECT r.partId, p.name AS partName, r.quantity "
                + "FROM requests r "
                + "JOIN parts p ON r.partId = p.partsId";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int partId = rs.getInt("partId");
                String partName = rs.getString("partName");
                int quantity = rs.getInt("quantity");

                WarehousePastRequests request = new WarehousePastRequests(partId, partName, quantity);
                partRequests.add(request);
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return partRequests;
    }


    /**
     * Checks if table is empty.
     * @return true if empty, else returns false.
     */
    public boolean isTableEmpty() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM requests");
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
