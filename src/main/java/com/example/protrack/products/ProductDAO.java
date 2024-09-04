package com.example.protrack.products;

import com.example.protrack.databaseutil.DatabaseConnection;
import com.example.protrack.users.AbstractUser;
import org.mindrot.jbcrypt.BCrypt;

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
                            + "name VARCHAR NOT NULL, "
                            + "dateCreated DATE NOT NULL, "
                            + "employeeId INTEGER NOT NULL, "
                            + "reqPartsId INTEGER NOT NULL, "
                            + "PIId INTEGER NOT NULL, "
                            + "status VARCHAR NOT NULL"
                            + ")"
            );

        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void newProduct(Product product) {
        try {
            PreparedStatement insertAccount = connection.prepareStatement(
                    "INSERT INTO products (productId, name, dateCreated, employeeId, reqPartsId, PIId, status) VALUES (?, ?, ?, ?, ?, ?, ?)"
            );

            insertAccount.setInt(1, product.getProductId());
            insertAccount.setString(2, product.getName());
            insertAccount.setDate(3, product.getDateCreated());
            insertAccount.setInt(4, product.getEmployeeId());
            insertAccount.setInt(5, product.getReqPartsId());
            insertAccount.setInt(6, product.getPIId());
            insertAccount.setString(7, product.getStatus());

            insertAccount.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
}
