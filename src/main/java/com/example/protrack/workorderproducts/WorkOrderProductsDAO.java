package com.example.protrack.workorderproducts;

import java.util.List;

/**
 * Interface for operations related to work order products.
 */
public interface WorkOrderProductsDAO {

    /**
     * Creates the work_order_products table in the database.
     */
    void createTable();

    /**
     * Adds a product to a work order.
     *
     * @param workOrderProduct The work order product to be added.
     * @return true if the operation was successful, false otherwise.
     */
    boolean addWorkOrderProduct(WorkOrderProduct workOrderProduct);

    /**
     * Deletes a product from a work order.
     *
     * @param workOrderProductId The ID of the work order product.
     * @return true if the operation was successful, false otherwise.
     */
    boolean deleteWorkOrderProduct(int workOrderProductId);

    /**
     * Retrieves all products associated with the specified work order.
     *
     * @param workOrderId The ID of the work order.
     * @return A list of products associated with the work order.
     */
    List<WorkOrderProduct> getWorkOrderProductsByWorkOrderId(int workOrderId);

    /**
     * Retrieves all work order products from the database.
     *
     * @return A list of all work order products.
     */
    List<WorkOrderProduct> getAllWorkOrderProducts();

    /**
     * Checks if the work_order_products table is empty.
     *
     * @return true if the table is empty, false otherwise.
     */
    boolean isTableEmpty();
}
