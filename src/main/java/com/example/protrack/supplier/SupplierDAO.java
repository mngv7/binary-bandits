package com.example.protrack.supplier;

import java.util.List;

/**
 * Interface for Supplier Data Access Object (DAO) that defines methods
 * for interacting with the suppliers data in the database.
 */
public interface SupplierDAO {

    /**
     * Creates the suppliers table in the database if it does not already exist.
     */
    void createTable();

    /**
     * Adds a new supplier to the database.
     *
     * @param supplier the Supplier object to be added
     */
    void addSupplier(Supplier supplier);

    /**
     * Retrieves a supplier from the database by its ID.
     *
     * @param supplierId the ID of the supplier to retrieve
     * @return the Supplier object corresponding to the specified ID
     */
    Supplier getSupplier(int supplierId);

    /**
     * Retrieves all suppliers from the database.
     *
     * @return a list of all Supplier objects
     */
    List<Supplier> getAllSuppliers();

    /**
     * Updates an existing supplier in the database.
     *
     * @param supplier the Supplier object with updated information
     */
    void updateSupplier(Supplier supplier);

    /**
     * Deletes a supplier from the database by its ID.
     *
     * @param supplierId the ID of the supplier to delete
     */
    void deleteSupplier(int supplierId);
}
