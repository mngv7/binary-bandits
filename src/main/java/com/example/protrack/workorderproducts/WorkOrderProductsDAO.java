package com.example.protrack.workorderproducts;

import com.example.protrack.products.Product;
import com.example.protrack.workorder.WorkOrder;

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
     * @param workOrder The work order which the product will be added to.
     * @param product   The product to be added.
     * @param quantity  The quantity of the product to be added.
     * @return true if the operation was successful, false otherwise.
     */
    boolean addWorkOrderProduct(WorkOrder workOrder, Product product, Integer quantity);

    /**
     * Retrieves all products associated with the specified work order.
     *
     * @param workOrderId The ID of the work order.
     * @return A list of products associated with the work order.
     */
    List<Product> getWorkOrderProductsByWorkOrderId(int workOrderId);

    /**
     * Deletes a product from a work order.
     *
     * @param workOrderId The ID of the work order.
     * @param productId   The ID of the product to be deleted.
     * @return true if the operation was successful, false otherwise.
     */
    boolean deleteWorkOrderProduct(int workOrderId, int productId);
}