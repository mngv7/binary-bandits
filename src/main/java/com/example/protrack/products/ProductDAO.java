package com.example.protrack.products;

import com.example.protrack.utility.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private final Connection connection;

    public ProductDAO() {
        connection = DatabaseConnection.getInstance();
    }

    public void createTable() {
        try {
            // Create a statement object for sending SQL queries to the database
            Statement createTable = connection.createStatement();

            // Execute the SQL query to create a table named 'products' if it does not already exist
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS products ("
                            + "productId INTEGER PRIMARY KEY, "
                            + "productName VARCHAR NOT NULL, "
                            + "dateCreated DATE NOT NULL"
                            + ")"
            );
        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that may occur during table creation
            System.err.println(ex);
        }
    }

    /**
     * Deletes product table
     */
    public void dropTable() {
        String query = "DROP TABLE IF EXISTS products";  // SQL statement to drop the work_orders table

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);    // executes SQL deletion statement
            System.out.println("Table 'products' dropped successfully.");
        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that may occur during table creation
            System.err.println("Error dropping table 'products': " + ex.getMessage());
        }
    }

    /**
     * Adds new product to product table
     * @param product a product being added to the table
     */
    public void newProduct(Product product) {
        try {
            PreparedStatement insertProduct = connection.prepareStatement(
                    "INSERT INTO products (productId, productName, dateCreated) VALUES (?, ?, ?)"
            );

            // sets product values into statement
            insertProduct.setInt(1, product.getProductId());
            insertProduct.setString(2, product.getProductName());
            insertProduct.setDate(3, product.getDateCreated());

            // executes and inserts product into product table
            insertProduct.execute();
        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that may occur during table creation
            System.err.println(ex);
        }
    }

    /**
     * Getter that gets all products of table
     * @return all products in product table
     */
    public List<Product> getAllProducts() {
        // empty list of products
        List<Product> products = new ArrayList<>();

        // query being run, get all from products
        String query = "SELECT * FROM products";

        // try running query
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // while there is a row, get those values and add it to the list
            while (rs.next()) {
                int productId = rs.getInt("productId");
                String productName = rs.getString("productName");
                Date dateCreated = rs.getDate("dateCreated");

                Product product = new Product(productId, productName, dateCreated);
                products.add(product);
            }
        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that may occur during table creation
            System.err.println(ex);
        }
        // return list of products in table
        return products;
    }


    /**
     * Checks if table is empty.
     * @return true if empty, else returns false.
     */
    public boolean isTableEmpty() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM products");
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
