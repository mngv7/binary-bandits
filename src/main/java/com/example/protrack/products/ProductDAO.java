package com.example.protrack.products;

import com.example.protrack.databaseutil.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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
}
