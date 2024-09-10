package com.example.protrack.workorder;


import com.example.protrack.customer.Customer;
import com.example.protrack.databaseutil.DatabaseConnection;
import com.example.protrack.users.AbstractUser;
import com.example.protrack.users.ProductionUser;
import javafx.scene.chart.PieChart;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class WorkOrdersDAOImplementation implements WorkOrdersDAO.WorkOrdersDAOInterface {
    private final Connection connection;
    private final HashMap<Integer, ProductionUser> productionUsers;
    private final HashMap<Integer, Customer> customers;

    public WorkOrdersDAOImplementation(HashMap<Integer, ProductionUser> productionUsers, HashMap<Integer, Customer> customers) {
        connection = DatabaseConnection.getInstance();
        this.productionUsers = productionUsers;
        this.customers = customers;

        if (connection == null) {
            throw new IllegalArgumentException("Invalid database connection");
        }
    }

    public void createTable() {
        try {
            Statement createTable = connection.createStatement();
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

    public void dropTable() {
        String query = "DROP TABLE IF EXISTS work_orders";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);
            System.out.println("Table 'work_orders' dropped successfully.");
        } catch (SQLException ex) {
            System.err.println("Error dropping table 'work_orders': " + ex.getMessage());
        }
    }

    public boolean createWorkOrder(WorkOrder workOrder) throws SQLException {
        String sqlNewWorkOrder = "INSERT INTO work_orders (work_order_owner_id, customer_id, order_date, delivery_date, shipping_address, status, subtotal) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlNewWorkOrder)) {

            preparedStatement.setInt(1, workOrder.getOrderOwner().getEmployeeId());
            preparedStatement.setInt(2, workOrder.getCustomer().getCustomerId());
            preparedStatement.setObject(3, workOrder.getOrderDate());
            preparedStatement.setObject(4, workOrder.getDeliveryDate());
            preparedStatement.setString(5, workOrder.getShippingAddress());
            preparedStatement.setString(6, workOrder.getStatus());
            preparedStatement.setDouble(7, workOrder.getSubtotal());

            int success = preparedStatement.executeUpdate();
            return success > 0;
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

    public ArrayList<WorkOrder> getWorkOrderByStatus(String status) throws SQLException {
        String sqlAllWorkOrders = "SELECT * FROM work_orders WHERE status = ?";
        ArrayList<WorkOrder> allWorkOrders = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlAllWorkOrders)) {
            preparedStatement.setString(1, status);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    WorkOrder workOrder = mapToWorkOrder(resultSet);
                    allWorkOrders.add(workOrder);
                }
            }
        }
        return allWorkOrders;
    }

    private WorkOrder mapToWorkOrder(ResultSet resultSet) throws SQLException {
        Integer workOrderId = resultSet.getInt("work_order_id");
        Integer orderOwnerId = resultSet.getInt("work_order_owner_id"); // Adjust if needed
        Integer customerId = resultSet.getInt("customer_id");

        ProductionUser orderOwner = productionUsers.get(orderOwnerId);
        Customer customer = customers.get(customerId);

        LocalDateTime orderDate = resultSet.getObject("order_date", LocalDateTime.class);
        LocalDateTime deliveryDate = resultSet.getObject("delivery_date", LocalDateTime.class);
        String shippingAddress = resultSet.getString("shipping_address");
        Integer productId = resultSet.getInt("products_id");
        String status = resultSet.getString("status");
        Double subtotal = resultSet.getDouble("subtotal");

        return new WorkOrder(
                workOrderId,
                orderOwner, // Create ProductionUser instance accordingly
                customer, // Create Customer instance accordingly
                orderDate,
                deliveryDate,
                shippingAddress,
                productId, // Assuming products are handled separately
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
