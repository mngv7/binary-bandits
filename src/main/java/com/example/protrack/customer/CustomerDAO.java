package com.example.protrack.customer;

import com.example.protrack.utility.DatabaseConnection;
import com.example.protrack.workorder.WorkOrder;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * DAO for managing Customer entities in the database
 * Provides methods for CRUD operations on customer-related data
 */
public class CustomerDAO {
    private final Connection connection;

    /**
     * Initializes the DAO with a database connection
     */
    public CustomerDAO() {
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
     * @throws SQLException If an SQL error occurs
     */
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

            // Executes the insert statement and returns true if the insert was successful
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Retrieves a customer from the database based on the provided customer ID.
     *
     * @param customerId The ID of the customer to be retrieved.
     * @return The Customer object if found, otherwise null.
     * @throws SQLException If an SQL error occurs.
     */
    public Customer getCustomer(Integer customerId) throws SQLException {
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
     * @return 'HashMap<Integer, Customer>' containing all customers.
     * @throws SQLException If an SQL error occurs during the operation.
     */
    public List<Customer> getAllCustomers() throws SQLException {
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
     * @throws SQLException If an SQL error occurs.
     */
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

    /**
     * Updates an existing customer in the database.
     *
     * @param customer The Customer object containing the (to be) updated details.
     * @throws SQLException If an SQL error occurs.
     */
    public void updateCustomer(Customer customer) throws SQLException {

    }

    /**
     * Deletes a customer from the database.
     *
     * @return true if the customer was deleted, false otherwise.
     */
    public boolean deleteCustomer() {
        return true;
    }

    /**
     * Searches for customers based on a query string from a search box.
     *
     * @param query The search query.
     * @return An ArrayList of Customer objects alike to the search.
     */
    public ArrayList<Customer> searchCustomers(String query) {
        return null;
    }

    /**
     * Retrieves all work orders associated with a specific customer.
     *
     * @param customerId The ID of the customer whose work orders are to be retrieved.
     * @return An ArrayList of WorkOrder objects associated with the customer.
     * @throws SQLException If an SQL error occurs during the operation.
     */
    public ArrayList<WorkOrder> getOrdersForCustomer(Integer customerId) throws SQLException {
        return null;
    }

}
