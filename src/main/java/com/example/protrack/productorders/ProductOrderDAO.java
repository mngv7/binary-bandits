package com.example.protrack.productorders;

import com.example.protrack.utility.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductOrderDAO {
    private final Connection connection;

    public ProductOrderDAO() {
        connection = DatabaseConnection.getInstance();
    }

    public void createTable() {
        try {
            // Create a statement object for sending SQL queries to the database
            Statement createTable = connection.createStatement();

            // Execute the SQL query to create a table named 'productBuild' if it does not already exist
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS productOrder ("
                            + "productOrderID INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "productID INTEGER NOT NULL, "
                            + "quantity INTEGER NOT NULL, "
                            + "workOrderID INTEGER NOT NULL"
                            + ")"
            );
        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that may occur during table creation
            System.err.println(ex);
        }
    }

    /**
     * Inserts inputted product order into product order table
     *
     * @param productOrder a product order for products
     */
    public void newProductOrder(ProductOrder productOrder) {
        try {
            PreparedStatement insertPO = connection.prepareStatement(
                    "INSERT INTO productOrder (productOrderID, productID, quantity, workOrderID) "
                            + "VALUES (?, ?, ?, ?)"
            );

            // sets test record value into statement
            insertPO.setInt(1, productOrder.getProductOrderID());
            insertPO.setInt(2, productOrder.getProductID());
            insertPO.setInt(3, productOrder.getQuantity());
            insertPO.setInt(4, productOrder.getWorkOrderID());

            // executes statement and enters test record values into test record table
            insertPO.execute();
        } catch (SQLException ex) {
            // print error if error occurs
            System.err.println(ex);
        }
    }

    /**
     * Deletes productOrder table
     */
    public void dropTable() {
        String query = "DROP TABLE IF EXISTS productOrder";  // SQL statement to drop the productOrder table

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);    // executes SQL deletion statement
            System.out.println("Table 'productOrder' dropped successfully.");
        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that may occur during table creation
            System.err.println("Error dropping table 'productOrder': " + ex.getMessage());
        }
    }

    /**
     * Getter that gets all productOrder of table
     *
     * @return all products in productOrder table
     */
    public List<ProductOrder> getAllProductOrder() {
        // empty list of Product Orders
        List<ProductOrder> productOrders = new ArrayList<>();

        // query being run, get all from productOrder
        String query = "SELECT * FROM productOrder";

        // try running query
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // while there is a row, get those values and add it to the list
            while (rs.next()) {
                int productOrderID = rs.getInt("productOrderID");
                int productID = rs.getInt("productID");
                int quantity = rs.getInt("quantity");
                int workOrderID = rs.getInt("workOrderID");

                ProductOrder productOrder = new ProductOrder(productOrderID, productID, quantity, workOrderID);
                productOrders.add(productOrder);
            }
        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that may occur during table creation
            System.err.println(ex);
        }
        // return list of productOrder in table
        return productOrders;
    }


    /**
     * Getter that gets certain productOrder of table
     * and it's WorkOrderID
     *
     * @return WorkOrderID of productOrder in productOrder table
     */
    public int getWOIDFromProductOrderID(int productOrderID) {

        int workOrderID = -1;

        // query being run, get all from productOrder
        String query = "SELECT * FROM productOrder WHERE productOrderID = ?";

        // try running query
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productOrderID); // Set the product ID in the query
            ResultSet rs = stmt.executeQuery();

            // Loop through the result set and populate the HashMap
            while (rs.next()) {
                workOrderID = rs.getInt("workOrderID");
            }
        } catch (SQLException ex) {
            //System.err.println("Error retrieving parts and amounts for product ID " + productId + ": " + ex.getMessage());
            System.out.println(ex);
        }

        // return workOrderID from productOrder in table
        return workOrderID;
    }

    /**
     * Checks if table is empty.
     *
     * @return true if empty, else returns false.
     */
    public boolean isTableEmpty() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM productOrder");
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
