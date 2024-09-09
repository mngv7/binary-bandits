package com.example.protrack.workorder;

import com.example.protrack.customer.Customer;
import com.example.protrack.users.ProductionUser;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class WorkOrdersDAO {

    public interface WorkOrdersDAOInterface {
        ArrayList<WorkOrder> getAllWorkOrders() throws SQLException;

        WorkOrder getWorkOrder(Integer id) throws SQLException;

        ArrayList<WorkOrder> getWorkOrderByStatus() throws SQLException;

        boolean createWorkOrder(WorkOrder workOrder) throws SQLException;

        boolean updateWorkOrder(WorkOrder workOrder) throws SQLException;

        boolean deleteWorkOrder(Integer id) throws SQLException;
    }


    public static class WorkOrdersDAOImplementation implements WorkOrdersDAOInterface {
        private final Connection connection;
        private final HashMap<Integer, ProductionUser> users;
        private final HashMap<Integer, Customer> customers;


        public WorkOrdersDAOImplementation(Connection connection, HashMap<Integer, ProductionUser> users, HashMap<Integer, Customer> customers) {
            this.connection = connection;
            this.users = users;
            this.customers = customers;
        }


        public void createTable() {
            try {
                Statement createTable = connection.createStatement();
                createTable.execute(
                        "CREATE TABLE IF NOT EXISTS work_orders ("
                                + "work_order_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                                + "work_order_owner VARCHAR, "
                                + "customer_name VARCHAR NOT NULL, "
                                + "order_date DATETIME NOT NULL, "
                                + "delivery_date DATETIME, "
                                + "shipping_address VARCHAR NOT NULL, "
                                + "products_id INTEGER NOT NULL , "
                                + "status VARCHAR NOT NULL , "
                                + "subtotal DOUBLE NOT NULL , "
                                + ")"
                );
            } catch (SQLException ex) {
                System.err.println(ex);
            }
        }

        public boolean createWorkOrder(WorkOrder workOrder) throws SQLException {
            String sqlNewWorkOrder = "INSERT INTO work_orders (work_order_id, order_owner_id, customer_id, order_date, delivery_date, shipping_address, status, subtotal) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlNewWorkOrder)) {
                //ID not set as DB handles this
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

        public ArrayList<WorkOrder> getWorkOrderByStatus() throws SQLException {
            return null;
        }

        private WorkOrder mapToWorkOrder(ResultSet resultSet) throws SQLException {
            Integer workOrderId = resultSet.getInt("work_order_id");
            Integer orderOwnerId = resultSet.getInt("order_owner_id"); // Adjust if needed
            Integer customerId = resultSet.getInt("customer_id");

            ProductionUser orderOwner = users.get(orderOwnerId);
            Customer customer = customers.get(customerId);

            LocalDateTime orderDate = resultSet.getObject("order_date", LocalDateTime.class);
            LocalDateTime deliveryDate = resultSet.getObject("delivery_date", LocalDateTime.class);
            String shippingAddress = resultSet.getString("shipping_address");
            String status = resultSet.getString("status");
            Double subtotal = resultSet.getDouble("subtotal");

            return new WorkOrder(
                    workOrderId,
                    orderOwner, // Create ProductionUser instance accordingly
                    customer, // Create Customer instance accordingly
                    orderDate,
                    deliveryDate,
                    shippingAddress,
                    new ArrayList<>(), // Assuming products are handled separately
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
}
