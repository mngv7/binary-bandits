package com.example.protrack.workorderproducts;

import com.example.protrack.products.Product;
import com.example.protrack.utility.DatabaseConnection;
import com.example.protrack.workorder.WorkOrder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface to the database relevant to WorkOrder operations, providing abstraction
 */
public class WorkOrderProductsDAOImplementation implements WorkOrderProductsDAO {

    // Connection to the database
    private final Connection connection;

    // Constructor initializes the JDBC connection using the singleton instance
    public WorkOrderProductsDAOImplementation() {
        connection = DatabaseConnection.getInstance();
    }

    /**
     * Creates the work_order_products table in the database with a composite key
     */
    public void createTable() {
        String sqlCreateTable = """
            CREATE TABLE IF NOT EXISTS work_order_products (
                work_order_id INTEGER, 
                product_id INTEGER, 
                quantity INTEGER NOT NULL, 
                PRIMARY KEY (work_order_id, product_id), 
                FOREIGN KEY (work_order_id) REFERENCES WorkOrder(work_order_id), 
                FOREIGN KEY (product_id) REFERENCES Product(product_id)
            );
        """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sqlCreateTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a product to a work order in the work_order_products table
     */
    public boolean addWorkOrderProduct(WorkOrder workOrder, Product product, Integer quantity) {
        String sqlAddWorkOrderProduct = "INSERT INTO work_order_products (work_order_id, product_id, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlAddWorkOrderProduct)) {
            preparedStatement.setInt(1, workOrder.getWorkOrderId());
            preparedStatement.setInt(2, product.getProductId());
            preparedStatement.setInt(3, quantity);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves all products associated with the specified work order
     */
    public List<Product> getWorkOrderProductsByWorkOrderId(int workOrderId) {
        String sqlGetWorkOrderProducts = """
            SELECT p.product_id, p.product_name, wop.quantity 
            FROM Product p 
            JOIN work_order_products wop ON p.product_id = wop.product_id 
            WHERE wop.work_order_id = ?;
        """;

        // Initialises list that will store products in the work order
        List<Product> productsInWorkOrder = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlGetWorkOrderProducts)) {
            // Sets the SQL value to the desired work order ID
            preparedStatement.setInt(1, workOrderId);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Iterates over the ResultSet returned from the executed SQL query, creating new Product instances with
            // information stored on the database and adding it to a list of products for the work order
            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("product_id"),
                        resultSet.getString("product_name"),
                        resultSet.getDate("quantity")
                );
                productsInWorkOrder.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productsInWorkOrder;
    }

    /**
     * Deletes a product from a work order
     */
    public boolean deleteWorkOrderProduct(int workOrderId, int productId) {
        String sqlDeleteWorkOrderProduct = "DELETE FROM work_order_products WHERE work_order_id = ? AND product_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteWorkOrderProduct)) {
            preparedStatement.setInt(1, workOrderId);
            preparedStatement.setInt(2, productId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}