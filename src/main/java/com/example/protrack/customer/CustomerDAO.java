package com.example.protrack.customer;

import com.example.protrack.databaseutil.DatabaseConnection;
import com.example.protrack.workorder.WorkOrder;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CustomerDAO {
    private final Connection connection;

    public CustomerDAO() {
        connection = DatabaseConnection.getInstance();
    }

    public void createTable() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS customer ("
                            + "customerId INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "firstName VARCHAR NOT NULL, "
                            + "lastName VARCHAR NOT NULL, "
                            + "email VARCHAR NOT NULL, "
                            + "phoneNumber CHAR(10), "
                            + "billingAddress VARCHAR NOT NULL, "
                            + "shippingAddress VARCHAR NOT NULL , "
                            + "status VARCHAR NOT NULL , "
                            + "orders INTEGER NOT NULL , "
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public boolean addCustomer(Customer customer) throws SQLException {
        return true;
    }

    public Customer getCustomer(Integer customerId) throws SQLException {
        return null;
    }

    public ArrayList<Customer> getAllCustomers() throws SQLException {
        return null;
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
