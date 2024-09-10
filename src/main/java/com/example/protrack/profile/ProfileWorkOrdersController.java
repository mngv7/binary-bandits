package com.example.protrack.profile;

import com.example.protrack.customer.Customer;
import com.example.protrack.databaseutil.DatabaseConnection;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.workorder.WorkOrder;
import com.example.protrack.workorder.WorkOrdersDAOImplementation;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ProfileWorkOrdersController {

    @FXML
    private ListView<WorkOrder> pendingWorkOrdersListView;

    private WorkOrdersDAOImplementation workOrdersDAOimpl;

    public ProfileWorkOrdersController() {
        // Initialize the database connection
        Connection connection = DatabaseConnection.getInstance();

        // Initialize the HashMaps (this is a placeholder; you need to populate these with actual data)
        HashMap<Integer, ProductionUser> productionUsers = new HashMap<>();
        HashMap<Integer, Customer> customers = new HashMap<>();

        // Initialize WorkOrdersDAOImplementation with required parameters
        this.workOrdersDAOimpl = new WorkOrdersDAOImplementation(productionUsers, customers);
    }

    @FXML
    private void initialize() {
        // Ensure that this method is called after FXML is loaded
        displayPendingWorkOrders();
    }

    @FXML
    private void displayPendingWorkOrders() {
        ArrayList<WorkOrder> pendingWorkOrders = new ArrayList<>();
        try {
            pendingWorkOrders = workOrdersDAOimpl.getWorkOrderByStatus("pending");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (pendingWorkOrders != null) {
            pendingWorkOrdersListView.getItems().setAll(pendingWorkOrders);
        } else {
            pendingWorkOrdersListView.getItems().clear(); // Clear if null
        }
    }

    private void acceptWorkOrder() {

    }
}