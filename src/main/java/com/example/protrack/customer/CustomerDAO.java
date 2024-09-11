package com.example.protrack.customer;

import com.example.protrack.databaseutil.DatabaseConnection;
import com.example.protrack.users.AbstractUser;
import com.example.protrack.workorder.WorkOrder;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomerDAO {
    private final Connection connection;
    private HashMap<Integer, Customer> customers;

    public CustomerDAO() {
        connection = DatabaseConnection.getInstance();
    }

    public void createTable() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS customer ("
                            + "customer_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "first_name VARCHAR NOT NULL, "
                            + "last_name VARCHAR NOT NULL, "
                            + "email VARCHAR NOT NULL, "
                            + "phone_number CHAR(10), "
                            + "billing_address VARCHAR NOT NULL, "
                            + "shipping_address VARCHAR NOT NULL , "
                            + "status VARCHAR NOT NULL "
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public boolean addCustomer(Customer customer) throws SQLException {
        String query = "INSERT INTO customer (first_name, last_name, email, phone_number, billing_address, shipping_address, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set parameters for the prepared statement
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPhoneNumber());
            stmt.setString(5, customer.getBillingAddress());
            stmt.setString(6, customer.getShippingAddress());
            stmt.setString(7, customer.getStatus());

            // Execute the insert statement
            int rowsAffected = stmt.executeUpdate();

            // Return true if the insert was successful
            return rowsAffected > 0;

        } catch (SQLException e) {
            // Handle SQL exception
            e.printStackTrace();
            throw e;
        }
    }

    public Customer getCustomer(Integer customerId) throws SQLException {
        return null;
    }

    public void dropTable() {
        String query = "DROP TABLE IF EXISTS customer";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);
            System.out.println("Table 'customers' dropped successfully.");
        } catch (SQLException ex) {
            System.err.println("Error dropping table 'users': " + ex.getMessage());
        }
    }

    public HashMap<Integer, Customer> getAllCustomers() throws SQLException {
        HashMap<Integer, Customer> customers = new HashMap<>();
        String query = "SELECT * FROM customer";

        try (Connection conn = DatabaseConnection.getInstance();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Customer customer = mapResultSetToCustomer(rs);
                customers.put(customer.getCustomerId(), customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        int customerId = rs.getInt("customer_id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String email = rs.getString("email");
        String phoneNumber = rs.getString("phone_number");
        String billingAddress = rs.getString("billing_address");
        String shippingAddress = rs.getString("shipping_address");
        String status = rs.getString("status");

        return new Customer(customerId, firstName, lastName, email, phoneNumber, billingAddress, shippingAddress, status);
    }

    public void updateCustomer(Customer customer) throws SQLException {

    }

    public boolean deleteCustomer() {
        return true;
    }

    public ArrayList<Customer> searchCustomers(String query) {
        return null;
    }

    public ArrayList<WorkOrder> getOrdersForCustomer(Integer customerId) throws SQLException {
        return null;
    }

}
