package com.example.protrack.products;

import java.util.List;

public interface IProductsDAO {
    // Method to create a table in the database
    void createTable();

    // Method to drop a table (optional, based on your `dropTable` in ProductDAO)
    void dropTable();

    // Method to insert a new record into the respective table
    void newProduct(Product product);
    void newTestRecordStep(TestRecord testRecordStep);
    void newRequiredParts(BillOfMaterials billOfMaterials);

    // Method to check if the table is empty
    boolean isTableEmpty();

    // Method to retrieve all products (ProductDAO specific)
    List<Product> getAllProducts();
}
