package com.example.protrack.workorder;


import com.example.protrack.customer.Customer;
import com.example.protrack.databaseutil.DatabaseConnection;
import com.example.protrack.users.AbstractUser;
import com.example.protrack.users.ProductionUser;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Interface to the database relevant to WorkOrder operations, providing abstraction
 */
public class WorkOrdersDAOImplementation implements WorkOrdersDAO.WorkOrdersDAOInterface {

    // Instantiation of necessary class variables
    private final Connection connection;
    private final HashMap<Integer, ProductionUser> productionUsers;
    private final HashMap<Integer, Customer> customers;

    // Constructor for WorkOrdersDAOImplementation
    public WorkOrdersDAOImplementation(HashMap<Integer, ProductionUser> productionUsers, HashMap<Integer, Customer> customers) throws SQLException {

        // Initialises the JDBC connection using the singleton instance
        connection = DatabaseConnection.getInstance();

        // Assigns class HashMap variables to instance variables
        this.productionUsers = productionUsers;
        this.customers = customers;
    }

    /**
     * Creates table work_orders in the database using the JDBC connection instance
     */
    public void createTable() {
        try {
            Statement createTable = connection.createStatement();

            // Executes the work_orders table creation SQL query with relevant attributes
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS work_orders ("
                            + "work_order_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "work_order_owner_id INTEGER, "
                            + "customer_id INTEGER NOT NULL, "
                            + "order_date DATETIME NOT NULL, "
                            + "delivery_date DATETIME, "
                            + "shipping_address VARCHAR NOT NULL, "
                            + "products_id INTEGER , "
                            + "status VARCHAR NOT NULL , "
                            + "subtotal DOUBLE NOT NULL "
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    /**
     * Drops the work_orders table from the database
     */
    public void dropTable() {
        String query = "DROP TABLE IF EXISTS work_orders";  // SQL statement to drop the work_orders table

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);    // executes SQL deletion statement
            System.out.println("Table 'work_orders' dropped successfully.");
        } catch (SQLException ex) {
            System.err.println("Error dropping table 'work_orders': " + ex.getMessage());
        }
    }

    /**
     *
     * @param workOrder the WorkOrder to be inserted into the database
     * @return true if at least one row is affected from the SQL insertion, otherwise false
     * @throws SQLException if the SQL query fails
     */
    public boolean createWorkOrder(WorkOrder workOrder) throws SQLException {
        String sqlNewWorkOrder = "INSERT INTO work_orders (work_order_owner_id, customer_id, order_date, delivery_date, shipping_address, status, subtotal) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlNewWorkOrder)) {

            // Sets parameters for the PreparedStatement
            preparedStatement.setInt(1, workOrder.getOrderOwner().getEmployeeId());
            preparedStatement.setInt(2, workOrder.getCustomer().getCustomerId());
            preparedStatement.setObject(3, workOrder.getOrderDate());
            preparedStatement.setObject(4, workOrder.getDeliveryDate());
            preparedStatement.setString(5, workOrder.getShippingAddress());
            preparedStatement.setString(6, workOrder.getStatus());
            preparedStatement.setDouble(7, workOrder.getSubtotal());

            // Execute the PreparedStatement and return true if at least one row is affected, otherwise false
            return preparedStatement.executeUpdate() > 0;
        }
    }

    public WorkOrder getWorkOrder(Integer workOrderId) throws SQLException {
        return null;
    }

    public ArrayList<WorkOrder> getAllWorkOrders() throws SQLException {
        String sqlAllWorkOrders = "SELECT * FROM work_orders";
        ArrayList<WorkOrder> allWorkOrders = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlAllWorkOrders);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                WorkOrder workOrder = mapToWorkOrder(resultSet);
                allWorkOrders.add(workOrder);
            }
        }
        return allWorkOrders;
    }

    /**
     *
     * @param status String containing the status of the Work Order
     * @return 'ArrayList<WorkOrder>'  all orders that satisfy the status
     * @throws SQLException if the SQL Prepared Statement fails
     */
    public ArrayList<WorkOrder> getWorkOrderByStatus(String status) throws SQLException {

        // SQL query retrieves Work Orders based on status
        String sqlAllWorkOrders = "SELECT * FROM work_orders WHERE status = ?";

        // Initialises the to-be-returned ArrayList<WorkOrder>
        ArrayList<WorkOrder> allWorkOrders = new ArrayList<>();

        // Executes the PreparedStatement with the status String
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlAllWorkOrders)) {
            preparedStatement.setString(1, status);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                // Iterates through each returned row (ResultSet) from the SQL query execution
                while (resultSet.next()) {

                    // Calls mapToWorkOrder(resultSet) to return a new WorkOrder instance, instantiated with the returned ResultSet information
                    WorkOrder workOrder = mapToWorkOrder(resultSet);
                    allWorkOrders.add(workOrder);   // Adds the new WorkOrder instance to the ArrayList
                }
            }
        }
        return allWorkOrders;   // Returns all new WorkOrder instances from the SQL query execution as an ArrayList<WorkOrder>
    }

    /**
     *
     * @param resultSet value returned from a SQL PreparedStatement execution
     * @return new WorkOrder instance populated with the values from the resultSet
     * @throws SQLException for if an SQL operation fails
     */
    private WorkOrder mapToWorkOrder(ResultSet resultSet) throws SQLException {

        // Maps the returned SQL query (resultSet) to relevant Work Order variables
        Integer workOrderId = resultSet.getInt("work_order_id");
        Integer orderOwnerId = resultSet.getInt("work_order_owner_id");
        Integer customerId = resultSet.getInt("customer_id");

        // Retrieves the relevant Production User as specified by the resultSet
        AbstractUser orderOwner = productionUsers.get(orderOwnerId);

        // Retrieves the relevant Production User as specified by the resultSet
        Customer customer = customers.get(customerId);

        LocalDateTime orderDate = resultSet.getObject("order_date", LocalDateTime.class);
        LocalDateTime deliveryDate = resultSet.getObject("delivery_date", LocalDateTime.class);
        String shippingAddress = resultSet.getString("shipping_address");
        Integer productId = resultSet.getInt("products_id");
        String status = resultSet.getString("status");
        Double subtotal = resultSet.getDouble("subtotal");

        // uses the new mapped variables to create a new WorkOrder instance
        return new WorkOrder(
                workOrderId,
                (ProductionUser) orderOwner,
                customer,
                orderDate,
                deliveryDate,
                shippingAddress,
                productId,
                status,
                subtotal
        );
    }

    public boolean updateWorkOrder(WorkOrder workOrder) throws SQLException {
        return true;
    }

    public boolean deleteWorkOrder(Integer workOrderId) throws SQLException {
        return true;
    }
}
