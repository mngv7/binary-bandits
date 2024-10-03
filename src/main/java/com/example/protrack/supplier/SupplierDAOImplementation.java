package com.example.protrack.supplier;

import com.example.protrack.utility.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAOImplementation implements SupplierDAO {
    private Connection connection;

    // Constructor that accepts a database connection
    public SupplierDAOImplementation() {
        connection = DatabaseConnection.getInstance();
    }

    // Method to create the suppliers table if it does not exist
    public void createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS suppliers (" +
                "supplier_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR(255), " +
                "email VARCHAR(255), " +
                "phone_number VARCHAR(20), " +
                "billing_address VARCHAR(255), " +
                "shipping_address VARCHAR(255), " +
                "lead_time DOUBLE)";
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addSupplier(Supplier supplier) {
        String sql = "INSERT INTO suppliers (name, email, phone_number, billing_address, shipping_address, lead_time) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, supplier.getName());
            statement.setString(2, supplier.getEmail());
            statement.setString(3, supplier.getPhoneNumber());
            statement.setString(4, supplier.getBillingAddress());
            statement.setString(5, supplier.getShippingAddress());
            statement.setDouble(6, supplier.getLeadTime());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Supplier getSupplier(int supplierId) {
        String sql = "SELECT * FROM suppliers WHERE supplier_id = ?";
        Supplier supplier = null;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, supplierId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                supplier = new Supplier(
                        resultSet.getInt("supplier_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("billing_address"),
                        resultSet.getString("shipping_address"),
                        resultSet.getDouble("lead_time")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return supplier;
    }

    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Supplier supplier = new Supplier(
                        resultSet.getInt("supplier_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("billing_address"),
                        resultSet.getString("shipping_address"),
                        resultSet.getDouble("lead_time")
                );
                suppliers.add(supplier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    public void updateSupplier(Supplier supplier) {
        String sql = "UPDATE suppliers SET name = ?, email = ?, phone_number = ?, billing_address = ?, shipping_address = ?, lead_time = ? WHERE supplier_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, supplier.getName());
            statement.setString(2, supplier.getEmail());
            statement.setString(3, supplier.getPhoneNumber());
            statement.setString(4, supplier.getBillingAddress());
            statement.setString(5, supplier.getShippingAddress());
            statement.setDouble(6, supplier.getLeadTime());
            statement.setInt(7, supplier.getSupplierId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSupplier(int supplierId) {
        String sql = "DELETE FROM suppliers WHERE supplier_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, supplierId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
