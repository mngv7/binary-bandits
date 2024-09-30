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
    public boolean addWorkOrderProduct(WorkOrderProduct workOrderProduct) {
        String sqlAddWorkOrderProduct = "INSERT INTO work_order_products (work_order_id, product_id, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlAddWorkOrderProduct)) {
            preparedStatement.setInt(1, workOrderProduct.getWorkOrderId());
            preparedStatement.setInt(2, workOrderProduct.getProductId());
            preparedStatement.setInt(3, workOrderProduct.getQuantity());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves all products associated with the specified work order
     */
    public List<WorkOrderProduct> getWorkOrderProductsByWorkOrderId(int workOrderId) {
        String sqlGetWorkOrderProducts =
            """ 
            SELECT p.productId AS product_id, 
                   p.productName AS product_name, 
                   wop.quantity, 
                   SUM(part.cost * bom.requiredAmount) AS totalPrice
            FROM products p
            JOIN work_order_products wop ON p.productId = wop.product_id
            LEFT JOIN requiredParts bom ON p.productId = bom.productId
            LEFT JOIN parts part ON bom.partsId = part.partsId
            WHERE wop.work_order_id = ?
            GROUP BY p.productId, p.productName, wop.quantity;
            """;

        // Initializes list that will store WorkOrderProduct instances
        List<WorkOrderProduct> productsInWorkOrder = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlGetWorkOrderProducts)) {
            // Sets the SQL value to the desired work order ID
            preparedStatement.setInt(1, workOrderId);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Iterates over the ResultSet returned from the executed SQL query,
            // creating new WorkOrderProduct instances with information stored in the database
            while (resultSet.next()) {
                WorkOrderProduct workOrderProduct = new WorkOrderProduct(
                        workOrderId,                              // Work Order ID
                        resultSet.getInt("product_id"),          // Product ID
                        resultSet.getString("product_name"),      // Product name
                        resultSet.getInt("quantity"),             // Quantity
                        resultSet.getDouble("totalPrice")         // Total Price (corrected from "price" to "totalPrice")
                );
                productsInWorkOrder.add(workOrderProduct);
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

    public boolean isTableEmpty() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM work_order_products");
            rs.next();
            int count = rs.getInt("rowcount");
            rs.close();
            return count == 0;
        } catch (SQLException e) {
            System.err.println(e);
        }
        return false;
    }
}