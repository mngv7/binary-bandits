package com.example.protrack.customer;

import com.example.protrack.utility.DatabaseConnection;
import com.example.protrack.workorder.WorkOrder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for managing Customer entities in the database
 * Provides methods for CRUD operations on customer-related data
 */
public class CustomerDAOImplementation implements CustomerDAO {
    private final Connection connection;

    /**
     * Initializes the DAO with a database connection
     */
    public CustomerDAOImplementation() {
        connection = DatabaseConnection.getInstance();
    }

    /**
     * Creates the customer table in the database if it doesn't exist
     */
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

    public boolean isTableEmpty() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM customer");
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
     * Adds a new customer to the database
     *
     * @param customer The Customer object to be added
     * @return true if the customer was added successfully to the database, false otherwise
     */
    public boolean addCustomer(Customer customer) {
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

            // Executes the insert statement and returns true if the insert was successful
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves a customer from the database based on the provided customer ID.
     *
     * @param customerId The ID of the customer to be retrieved.
     * @return The Customer object if found, otherwise null.
     */
    public Customer getCustomer(Integer customerId) {
        return null; // Need to implement.
    }

    /**
     * Drops the customer table from the database if exists.
     */
    public void dropTable() {
        String query = "DROP TABLE IF EXISTS customer";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);
            System.out.println("Table 'customers' dropped successfully.");
        } catch (SQLException ex) {
            System.err.println("Error dropping table 'customers': " + ex.getMessage());
        }
    }

    /**
     * Retrieves all customers from the database.
     *
     * @return List containing all customers.
     */
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customer";

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Customer customer = mapResultSetToCustomer(rs);
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    /**
     * Maps a ResultSet row to a Customer object.
     *
     * @param rs The ResultSet containing customer data.
     * @return The Customer object mapped from the ResultSet.
     */
    private Customer mapResultSetToCustomer(ResultSet rs) {

        try {
            Integer customerId = rs.getInt("customer_id");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String email = rs.getString("email");
            String phoneNumber = rs.getString("phone_number");
            String billingAddress = rs.getString("billing_address");
            String shippingAddress = rs.getString("shipping_address");
            String status = rs.getString("status");

            return new Customer(customerId, firstName, lastName, email, phoneNumber, billingAddress, shippingAddress, status);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Updates an existing customer in the database.
     *
     * @param customer The Customer object containing the (to be) updated details.
     */
    public void updateCustomer(Customer customer) {
        String sql = "UPDATE customer SET first_name = ?, last_name = ?, email = ?, phone_number = ?, billing_address = ?, shipping_address = ?, status = ? WHERE customer_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getPhoneNumber());
            statement.setString(5, customer.getBillingAddress());
            statement.setString(6, customer.getShippingAddress());
            statement.setString(7, customer.getStatus());
            statement.setInt(8, customer.getCustomerId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a customer from the database.
     *
     * @param customerId the ID of the customer to be deleted.
     * @return true if the customer was deleted, false otherwise.
     */
    public boolean deleteCustomer(int customerId) {
        String sql = "DELETE FROM customer WHERE customer_id = ?";

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            int affectedRows = ps.executeUpdate();

            return affectedRows > 0; // Return true if at least one row was deleted
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if there was an error
        }
    }

    /**
     * Searches for customers based on a query string from a search box.
     *
     * @param query The search query.
     * @return A List of Customer objects alike to the search.
     */
    public List<Customer> searchCustomers(String query) {
        return null;
    }

    /**
     * Retrieves all work orders associated with a specific customer.
     *
     * @param customerId The ID of the customer whose work orders are to be retrieved.
     * @return A List of WorkOrder objects associated with the customer.
     */
    public List<WorkOrder> getOrdersForCustomer(Integer customerId) {
        return null;
    }

}
