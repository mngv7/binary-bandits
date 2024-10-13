package com.example.protrack.workorderproducts;

import com.example.protrack.utility.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the WorkOrderProductsDAO interface, providing access to the database
 * for operations related to work order products.
 */
public class WorkOrderProductsDAOImplementation implements WorkOrderProductsDAO {

    // Connection to the database
    private final Connection connection;

    /**
     * Initializes the WorkOrderProductsDAOImplementation and establishes a
     * connection to the database using the DatabaseConnection singleton instance.
     */
    public WorkOrderProductsDAOImplementation() {
        connection = DatabaseConnection.getInstance();
    }

    @Override
    public void createTable() {
        String sqlCreateTable = """
                    CREATE TABLE IF NOT EXISTS work_order_products (
                        work_order_product_id INTEGER PRIMARY KEY AUTOINCREMENT,
                        work_order_id INTEGER, 
                        product_id INTEGER, 
                        quantity INTEGER NOT NULL, 
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

    @Override
    public boolean addWorkOrderProduct(WorkOrderProduct workOrderProduct) {
        String sqlAddWorkOrderProduct = "INSERT INTO work_order_products (work_order_id, product_id, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlAddWorkOrderProduct, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, workOrderProduct.getWorkOrderId());
            preparedStatement.setInt(2, workOrderProduct.getProductId());
            preparedStatement.setInt(3, workOrderProduct.getQuantity());

            // Execute update and get the generated primary key
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        workOrderProduct.setWorkOrderProductId(generatedKeys.getInt(1));
                    }
                }
            }
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<WorkOrderProduct> getWorkOrderProductsByWorkOrderId(int workOrderId) {
        String sqlGetWorkOrderProducts =
                """ 
                        SELECT wop.work_order_product_id, 
                               p.productId AS product_id, 
                               p.productName AS product_name, 
                               wop.quantity, 
                               SUM(part.cost * bom.requiredAmount) AS totalPrice
                        FROM products p
                        JOIN work_order_products wop ON p.productId = wop.product_id
                        LEFT JOIN requiredParts bom ON p.productId = bom.productId
                        LEFT JOIN parts part ON bom.partsId = part.partsId
                        WHERE wop.work_order_id = ?
                        GROUP BY wop.work_order_product_id, p.productId, p.productName, wop.quantity;
                        """;

        List<WorkOrderProduct> productsInWorkOrder = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlGetWorkOrderProducts)) {
            preparedStatement.setInt(1, workOrderId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                WorkOrderProduct workOrderProduct = new WorkOrderProduct(
                        resultSet.getInt("work_order_product_id"),   // Work Order Product ID
                        workOrderId,                                            // Work Order ID
                        resultSet.getInt("product_id"),              // Product ID
                        resultSet.getString("product_name"),         // Product name
                        resultSet.getInt("quantity"),                // Quantity
                        resultSet.getDouble("totalPrice")            // Total Price
                );
                productsInWorkOrder.add(workOrderProduct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productsInWorkOrder;
    }

    @Override
    public List<WorkOrderProduct> getAllWorkOrderProducts() {
        String sqlGetAllWorkOrderProducts =
                """ 
                        SELECT wop.work_order_product_id, 
                               wop.work_order_id, 
                               p.productId AS product_id, 
                               p.productName AS product_name, 
                               wop.quantity, 
                               SUM(part.cost * bom.requiredAmount) AS totalPrice
                        FROM work_order_products wop
                        JOIN products p ON wop.product_id = p.productId
                        LEFT JOIN requiredParts bom ON p.productId = bom.productId
                        LEFT JOIN parts part ON bom.partsId = part.partsId
                        GROUP BY wop.work_order_product_id, wop.work_order_id, p.productId, p.productName, wop.quantity;
                        """;

        List<WorkOrderProduct> allWorkOrderProducts = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlGetAllWorkOrderProducts)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                WorkOrderProduct workOrderProduct = new WorkOrderProduct(
                        resultSet.getInt("work_order_product_id"),   // Work Order Product ID
                        resultSet.getInt("work_order_id"),                   // Work Order ID
                        resultSet.getInt("product_id"),              // Product ID
                        resultSet.getString("product_name"),         // Product name
                        resultSet.getInt("quantity"),                // Quantity
                        resultSet.getDouble("totalPrice")            // Total Price
                );
                allWorkOrderProducts.add(workOrderProduct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allWorkOrderProducts;
    }

    @Override
    public boolean deleteWorkOrderProduct(int workOrderProductId) {
        String sqlDeleteWorkOrderProduct = "DELETE FROM work_order_products WHERE work_order_product_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteWorkOrderProduct)) {
            preparedStatement.setInt(1, workOrderProductId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
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
