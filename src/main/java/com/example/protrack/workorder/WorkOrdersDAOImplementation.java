package com.example.protrack.workorder;


import com.example.protrack.customer.Customer;
import com.example.protrack.users.AbstractUser;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.utility.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface to the database relevant to WorkOrder operations, providing abstraction
 */
public class WorkOrdersDAOImplementation implements WorkOrdersDAO {

    // Instantiation of necessary class variables
    private final Connection connection;
    private final List<ProductionUser> productionUsers;
    private final List<Customer> customers;

    // Constructor for WorkOrdersDAOImplementation
    public WorkOrdersDAOImplementation(List<ProductionUser> productionUsers, List<Customer> customers) {

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
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public boolean isTableEmpty() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM work_orders");
            rs.next();
            int count = rs.getInt("rowcount");
            rs.close();
            return count == 0;
        } catch (SQLException e) {
            System.err.println(e);
        }
        return false;
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

    @Override
    public void createWorkOrder(WorkOrder workOrder) {
        String sqlNewWorkOrder = "INSERT INTO work_orders (work_order_owner_id, customer_id, order_date, delivery_date, shipping_address, status, subtotal) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlNewWorkOrder)) {

            // Sets parameters for the PreparedStatement
            // Check if orderOwner is null and set the appropriate value
            if (workOrder.getOrderOwner() != null) {
                preparedStatement.setInt(1, workOrder.getOrderOwner().getEmployeeId());
            } else {
                preparedStatement.setNull(1, java.sql.Types.INTEGER);  // Set NULL for the order_owner field if it's null
            }

            // other params
            preparedStatement.setInt(2, workOrder.getCustomer().getCustomerId());
            preparedStatement.setObject(3, workOrder.getOrderDate());
            preparedStatement.setObject(4, workOrder.getDeliveryDate());
            preparedStatement.setString(5, workOrder.getShippingAddress());
            preparedStatement.setObject(6, workOrder.getStatus());
            preparedStatement.setDouble(7, workOrder.getSubtotal());

            // Execute the PreparedStatement and return true if at least one row is affected, otherwise false
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<WorkOrder> getWorkOrdersByEmployeeId(int employeeId) {
        String sqlAllWorkOrders = "SELECT * FROM work_orders WHERE work_order_owner_id = ?";
        List<WorkOrder> workOrders = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlAllWorkOrders)) {
            preparedStatement.setInt(1, employeeId);

            ResultSet resultSet = preparedStatement.executeQuery();

            // Iterates through each returned row (ResultSet) from the SQL query execution
            while (resultSet.next()) {
                // Calls mapToWorkOrder(resultSet) to return a new WorkOrder instance, instantiated with the returned ResultSet information
                WorkOrder workOrder = mapToWorkOrder(resultSet);
                workOrders.add(workOrder);   // Adds the new WorkOrder instance to the ArrayList
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workOrders;
    }

    @Override
    public List<WorkOrder> getAllWorkOrders() {
        String sqlAllWorkOrders = "SELECT * FROM work_orders";
        List<WorkOrder> allWorkOrders = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlAllWorkOrders);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                WorkOrder workOrder = mapToWorkOrder(resultSet);
                allWorkOrders.add(workOrder);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allWorkOrders;
    }

    @Override
    public List<WorkOrder> getWorkOrderByStatus(String status) {

        // SQL query retrieves Work Orders based on status
        String sqlAllWorkOrders = "SELECT * FROM work_orders WHERE status = ?";

        List<WorkOrder> allWorkOrders = new ArrayList<>();

        // Executes the PreparedStatement with the status String
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlAllWorkOrders)) {
            preparedStatement.setString(1, status);

            ResultSet resultSet = preparedStatement.executeQuery();

            // Iterates through each returned row (ResultSet) from the SQL query execution
            while (resultSet.next()) {
                // Calls mapToWorkOrder(resultSet) to return a new WorkOrder instance, instantiated with the returned ResultSet information
                WorkOrder workOrder = mapToWorkOrder(resultSet);
                allWorkOrders.add(workOrder);   // Adds the new WorkOrder instance to the ArrayList
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allWorkOrders;   // Returns all new WorkOrder instances from the SQL query execution as an ArrayList<WorkOrder>
    }

    /**
     * @param resultSet value returned from a SQL PreparedStatement execution
     * @return new WorkOrder instance populated with the values from the resultSet
     */
    private WorkOrder mapToWorkOrder(ResultSet resultSet) {

        try {
            // Maps the returned SQL query (resultSet) to relevant Work Order variables
            Integer workOrderId = resultSet.getInt("work_order_id");
            Integer orderOwnerId = resultSet.getInt("work_order_owner_id");
            Integer customerId = resultSet.getInt("customer_id");

            // Retrieves the relevant Production User as specified by the resultSet
            AbstractUser orderOwner = null;
            for (AbstractUser user : productionUsers) {
                if (user.getEmployeeId().equals(orderOwnerId)) {
                    orderOwner = user;
                    break;
                }
            }

            // Retrieves the relevant Production User as specified by the resultSet
            Customer orderCustomer = null;
            for (Customer customer : customers) {
                if (customer.getCustomerId().equals(customerId)) {
                    orderCustomer = customer;
                    break;
                }
            }

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
                    orderCustomer,
                    orderDate,
                    deliveryDate,
                    shippingAddress,
                    status,
                    subtotal
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateWorkOrder(WorkOrder workOrder) {
        String query = "UPDATE work_orders SET work_order_owner_id = ?, customer_id = ?, order_date = ?, delivery_date = ?, " +
                "shipping_address = ?, status = ?, subtotal = ? WHERE work_order_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Order owner ID is nullable
            if (workOrder.getOrderOwner() != null) {
                statement.setObject(1, workOrder.getOrderOwner().getEmployeeId());
            } else {
                statement.setNull(1, Types.INTEGER);
            }

            statement.setInt(2, workOrder.getCustomer().getCustomerId());
            statement.setObject(3, workOrder.getOrderDate());

            // Delivery date is nullable
            if (workOrder.getDeliveryDate() != null) {
                statement.setObject(4, workOrder.getDeliveryDate());
            } else {
                statement.setNull(4, Types.DATE);
            }

            statement.setString(5, workOrder.getShippingAddress());
            statement.setString(6, workOrder.getStatus());
            statement.setDouble(7, workOrder.getSubtotal());

            statement.setInt(8, workOrder.getWorkOrderId());

            int rowsUpdated = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteWorkOrder(Integer workOrderId) {
        String query = "DELETE FROM work_orders WHERE work_order_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, workOrderId);

            int rowsDeleted = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a WorkOrder based on the specified customer and order date.
     * Since the primary key (workOrderId) is auto-incremented in the database,
     * this method uses a candidate key (customer ID and order date) to
     * retrieve the WorkOrder from the database.
     *
     * @param customer the Customer whose work order is to be retrieved
     * @param orderDate the date the order was placed
     * @return the WorkOrder associated with the specified customer and order date, or null if no
     * matching WorkOrder is found
     */
    public WorkOrder getWorkOrderByCustomerAndDate(Customer customer, LocalDateTime orderDate) {
        WorkOrder workOrder = null;

        // SQL query retrieves Work Orders based on status
        String sqlAllWorkOrders = "SELECT * FROM work_orders WHERE customer_id = ? AND order_date = ?";

        // Executes the PreparedStatement with the status String
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlAllWorkOrders)) {
            preparedStatement.setInt(1, customer.getCustomerId());
            preparedStatement.setObject(2, orderDate);

            ResultSet resultSet = preparedStatement.executeQuery();

            workOrder = mapToWorkOrder(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workOrder;
    }

    /**
     * Retrieves a list of WorkOrders placed in the specified month.
     *
     * @param month the month (1-12) for which WorkOrders should be retrieved
     * @return a List of WorkOrders placed in the specified month
     */
    public List<WorkOrder> getWorkOrderByMonth(Integer month) {
        List<WorkOrder> workOrders = new ArrayList<>();

        // SQL query retrieves Work Orders based on the month from order_date
        String sqlAllWorkOrders = "SELECT * FROM work_orders WHERE strftime('%m', order_date) = ?";

        // Executes the PreparedStatement with the month
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlAllWorkOrders)) {
            preparedStatement.setString(1, String.format("%02d", month));  // Format month as two digits (e.g., '01' for January)

            ResultSet resultSet = preparedStatement.executeQuery();

            // Loop through the result set and map each row to a WorkOrder
            while (resultSet.next()) {
                WorkOrder workOrder = mapToWorkOrder(resultSet);  // Assuming mapToWorkOrder maps a single result row to WorkOrder
                workOrders.add(workOrder);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return workOrders;
    }

    @Override
    public void updateWorkOrderStatus(int workOrderID, String newStatus) {
        String sqlUpdateWorkOrder = "UPDATE work_orders SET status = ? WHERE work_order_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateWorkOrder)) {
            preparedStatement.setString(1, newStatus);
            preparedStatement.setInt(2, workOrderID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
