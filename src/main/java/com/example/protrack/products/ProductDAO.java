package com.example.protrack.products;

import com.example.protrack.utility.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code ProductDAO} class provides methods for interacting with the products table in the database.
 */
public class ProductDAO {
    private final Connection connection; // Database connection

    /**
     * Initializes a new {@code ProductDAO} instance and establishes a connection to the database.
     */
    public ProductDAO() {
        connection = DatabaseConnection.getInstance();
    }

    /**
     * Creates the products table in the database if it does not already exist.
     */
    public void createTable() {
        try {
            // Create a statement object for sending SQL queries to the database
            Statement createTable = connection.createStatement();

            // Execute the SQL query to create a table named 'products'
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS products (" +
                            "productId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "productName VARCHAR NOT NULL, " +
                            "dateCreated DATE NOT NULL, " +
                            "price DOUBLE NOT NULL" +
                            ")"
            );
        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that may occur during table creation
            System.err.println(ex);
        }
    }

    /**
     * Drops the products table from the database if it exists.
     */
    public void dropTable() {
        String query = "DROP TABLE IF EXISTS products";  // SQL statement to drop the products table

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);    // Executes SQL deletion statement
            System.out.println("Table 'products' dropped successfully.");
        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that may occur during table dropping
            System.err.println("Error dropping table 'products': " + ex.getMessage());
        }
    }

    /**
     * Inserts a new product into the products table.
     *
     * @param product The product being added to the table.
     */
    public void newProduct(Product product) {
        try {
            PreparedStatement insertProduct = connection.prepareStatement(
                    "INSERT INTO products (productId, productName, dateCreated, price) VALUES (?, ?, ?, ?)"
            );

            // Sets product values into the prepared statement
            insertProduct.setInt(1, product.getProductId());
            insertProduct.setString(2, product.getProductName());
            insertProduct.setDate(3, product.getDateCreated());
            insertProduct.setDouble(4, product.getPrice()); // Set price

            // Executes and inserts product into the products table
            insertProduct.execute();
        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that may occur during product insertion
            System.err.println(ex);
        }
    }

    /**
     * Checks if the products table is empty.
     *
     * @return {@code true} if the table is empty, {@code false} otherwise.
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

    /**
     * Retrieves all products from the products table.
     *
     * @return A list of all products in the products table.
     */
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        String query = "SELECT * FROM products";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int productId = rs.getInt("productId");
                String productName = rs.getString("productName");
                Date dateCreated = rs.getDate("dateCreated");
                double price = rs.getDouble("price"); // Retrieve price directly from the products table

                // Create Product object with price
                Product product = new Product(productId, productName, dateCreated, price);
                products.add(product);
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return products;
    }

    /**
     * Calculates the total price of a product based on its parts and required amounts.
     *
     * @param productId The ID of the product to calculate the price for.
     * @return The total calculated price of the product.
     */
    public double calculateProductPrice(int productId) {
        double totalPrice = 0.0;

        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT p.cost, bm.requiredAmount " +
                            "FROM requiredParts bm " +
                            "JOIN parts p ON bm.partsId = p.partsId " +
                            "WHERE bm.productId = ?"
            );
            statement.setInt(1, productId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                double partPrice = rs.getDouble("cost");
                int requiredAmount = rs.getInt("requiredAmount");
                totalPrice += partPrice * requiredAmount;
            }
        } catch (SQLException e) {
            System.err.println("Error calculating total price: " + e.getMessage());
        }

        return totalPrice;
    }

    /**
     * Updates the price of a product in the database.
     *
     * @param productId The ID of the product to update.
     * @param newPrice  The new price to set for the product.
     */
    public void updateProductPrice(int productId, double newPrice) {
        String updatePriceSQL = "UPDATE products SET price = ? WHERE productId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updatePriceSQL)) {
            preparedStatement.setDouble(1, newPrice);
            preparedStatement.setInt(2, productId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating product price: " + e.getMessage());
        }
    }
}
