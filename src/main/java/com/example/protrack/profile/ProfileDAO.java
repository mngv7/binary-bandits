package com.example.protrack.profile;

import com.example.protrack.applicationpages.Product;
import com.example.protrack.databaseutil.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ProfileDAO {

    private Connection connection;

    public ProfileDAO() {
        connection = DatabaseConnection.getInstance();
    }

    private HashMap<Integer, ArrayList<Product>> getPendingWorkOrders() throws SQLException {
        HashMap<Integer, ArrayList<Product>> pendingWorkOrders = new HashMap<>();
        String selectWorkOrders = "SELECT workOrderNumber, productList FROM WorkOrder WHERE type = 'pending' ORDER BY workOrderNumber ASC";
        try (PreparedStatement getPendingWorkOrders = connection.prepareStatement(selectWorkOrders))
        {
            ResultSet workOrders = getPendingWorkOrders.executeQuery();
            while (workOrders.next()) {
                pendingWorkOrders.put(workOrders.getInt(0), (ArrayList<Product>) workOrders.getObject(1));
            }
        } catch (SQLException e) {
            System.err.print(e);
        }
        return pendingWorkOrders;
    }
}
