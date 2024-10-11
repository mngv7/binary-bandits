package com.example.protrack.customer;

import com.example.protrack.workorder.WorkOrder;

import java.util.List;

/**
 * Interface for CustomerDAO operations.
 * Provides a contract for CRUD operations and customer-related queries.
 */
public interface CustomerDAO {

    /**
     * Creates the customer table in the database if it doesn't exist.
     */
    void createTable();

    /**
     * Checks if the customer table is empty.
     *
     * @return true if the customer table is empty, false otherwise.
     */
    boolean isTableEmpty();

    /**
     * Adds a new customer to the database.
     *
     * @param customer The Customer object to be added.
     * @return true if the customer was added successfully, false otherwise.
     */
    boolean addCustomer(Customer customer);

    /**
     * Retrieves a customer from the database based on the provided customer ID.
     *
     * @param customerId The ID of the customer to be retrieved.
     * @return The Customer object if found, otherwise null.
     */
    Customer getCustomer(Integer customerId);

    /**
     * Drops the customer table from the database if it exists.
     */
    void dropTable();

    /**
     * Retrieves all customers from the database.
     *
     * @return A List of Customer objects.
     */
    List<Customer> getAllCustomers();

    /**
     * Updates an existing customer in the database.
     *
     * @param customer The Customer object containing updated details.
     */
    void updateCustomer(Customer customer);

    /**
     * Deletes a customer from the database.
     *
     * @param customerId The ID of the customer to be deleted.
     * @return true if the customer was deleted, false otherwise.
     */
    boolean deleteCustomer(int customerId);

    /**
     * Searches for customers based on a query string.
     *
     * @param query The search query.
     * @return A List of Customer objects matching the search.
     */
    List<Customer> searchCustomers(String query);

    /**
     * Retrieves all work orders associated with a specific customer.
     *
     * @param customerId The ID of the customer whose work orders are to be retrieved.
     * @return A List of WorkOrder objects associated with the customer.
     */
    List<WorkOrder> getOrdersForCustomer(Integer customerId);
}
