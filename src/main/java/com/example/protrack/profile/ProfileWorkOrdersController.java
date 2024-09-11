package com.example.protrack.profile;

import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAO;
import com.example.protrack.databaseutil.DatabaseConnection;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.workorder.WorkOrder;
import com.example.protrack.workorder.WorkOrdersDAOImplementation;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ProfileWorkOrdersController {

    @FXML
    private ListView<WorkOrder> pendingWorkOrdersListView;

    private final WorkOrdersDAOImplementation workOrdersDAOimpl;

    public ProfileWorkOrdersController() throws SQLException {

        // Initialize the HashMaps (this is a placeholder; you need to populate these with actual data)
        UsersDAO usersDAO = new UsersDAO();
        CustomerDAO customerDAO = new CustomerDAO();

        HashMap<Integer, ProductionUser> productionUsers = usersDAO.getAllUsers();
        HashMap<Integer, Customer> customers = customerDAO.getAllCustomers();

        // Initialize WorkOrdersDAOImplementation with required parameters
        this.workOrdersDAOimpl = new WorkOrdersDAOImplementation(productionUsers, customers);
    }

    @FXML
    private void initialize() {
        // Ensure that this method is called after FXML is loaded
        System.out.println("initialize() called");
        displayPendingWorkOrders();
    }

    @FXML
    private void displayPendingWorkOrders() {
        ArrayList<WorkOrder> pendingWorkOrders;
        try {
            pendingWorkOrders = workOrdersDAOimpl.getWorkOrderByStatus("pending");
            System.out.println("Fetched Work Orders: " + pendingWorkOrders);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Set cell factory to customize how WorkOrder items are displayed
        pendingWorkOrdersListView.setCellFactory(listView -> new ListCell<>() {

            @Override
            protected void updateItem(WorkOrder item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Customize the cell content here
                    setText("Work Order ID: " + item.getWorkOrderId() +
                            "\nOrder Date: " + item.getOrderDate());
                }
            }
        });

        if (pendingWorkOrders != null) {
            pendingWorkOrdersListView.getItems().setAll(pendingWorkOrders);
        } else {
            pendingWorkOrdersListView.getItems().clear(); // Clear if null
        }
    }

    private void acceptWorkOrder() {

    }
}