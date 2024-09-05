package com.example.protrack.products;

import com.example.protrack.databaseutil.DatabaseConnection;

import java.sql.*;

public class ProductDAO {
    private Connection connection;

    public ProductDAO() {
        connection = DatabaseConnection.getInstance();
    }

    public void createTable() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS products ("
                            + "productId INTEGER PRIMARY KEY, "
                            + "productName VARCHAR NOT NULL, "
                            + "dateCreated DATE NOT NULL"
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void newProduct(Product product) {
        try {
            PreparedStatement insertProduct = connection.prepareStatement(
                    "INSERT INTO products (productId, productName, dateCreated) VALUES (?, ?, ?)"
            );

            insertProduct.setInt(1, product.getProductId());
            insertProduct.setString(2, product.getProductName());
            insertProduct.setDate(3, product.getDateCreated());

            insertProduct.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

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
