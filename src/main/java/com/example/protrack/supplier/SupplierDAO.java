package com.example.protrack.supplier;

import com.example.protrack.utility.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {
    private Connection connection;

    // Constructor that accepts a database connection
    public SupplierDAO() {
        connection = DatabaseConnection.getInstance();
    }

    public void addSupplier(Supplier supplier) {
        String sql = "INSERT INTO suppliers (name, email, phone_number, billing_address) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, supplier.getName());
            statement.setString(2, supplier.getEmail());
            statement.setString(3, supplier.getPhoneNumber());
            statement.setString(4, supplier.getBillingAddress());
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
                        resultSet.getString("billing_address")
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
                        resultSet.getString("billing_address")
                );
                suppliers.add(supplier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    public void updateSupplier(Supplier supplier) {
        String sql = "UPDATE suppliers SET name = ?, email = ?, phone_number = ?, billing_address = ? WHERE supplier_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, supplier.getName());
            statement.setString(2, supplier.getEmail());
            statement.setString(3, supplier.getPhoneNumber());
            statement.setString(4, supplier.getBillingAddress());
            statement.setInt(5, supplier.getSupplierId());
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
