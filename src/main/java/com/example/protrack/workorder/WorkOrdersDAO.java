package com.example.protrack.workorder;

import java.util.List;

/**
 * Interface for database operations related to work orders.
 */
public interface WorkOrdersDAO {

    /**
     * Retrieves all work orders from the database.
     *
     * @return A list of all work orders.
     */
    List<WorkOrder> getAllWorkOrders();

    /**
     * Retrieves work orders that match the specified status.
     *
     * @param status The status of the work orders to retrieve.
     * @return A list of work orders with the specified status.
     */
    List<WorkOrder> getWorkOrderByStatus(String status);

    List<WorkOrder> getWorkOrdersByEmployeeId(int employeeId);

    /**
     * Creates a new work order in the database.
     *
     * @param workOrder The work order to be created.
     * @return true if the work order was created successfully, false otherwise.
     */
    boolean createWorkOrder(WorkOrder workOrder);

    /**
     * Updates an existing work order in the database.
     *
     * @param workOrder The work order with updated information.
     * @return true if the work order was updated successfully, false otherwise.
     */
    boolean updateWorkOrder(WorkOrder workOrder);

    /**
     * Deletes a work order from the database based on its ID.
     *
     * @param id The ID of the work order to be deleted.
     * @return true if the work order was deleted successfully, false otherwise.
     */
    boolean deleteWorkOrder(Integer id);
}