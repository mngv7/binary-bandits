package com.example.protrack.supplier;

import java.util.List;

public interface SupplierDAO {

    // Method to create the suppliers table if it does not exist
    void createTable();

    // Method to add a new supplier
    void addSupplier(Supplier supplier);

    // Method to get a supplier by ID
    Supplier getSupplier(int supplierId);

    // Method to get all suppliers
    List<Supplier> getAllSuppliers();

    // Method to update an existing supplier
    void updateSupplier(Supplier supplier);

    // Method to delete a supplier by ID
    void deleteSupplier(int supplierId);
}
