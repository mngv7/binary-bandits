package com.example.protrack.profile;

import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAO;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.workorder.WorkOrder;
import com.example.protrack.workorder.WorkOrdersDAOImplementation;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Controller for managing and displaying work orders on the profile page.
 * The controller display of pending work orders with a List View, interacting with
 * User, Customer, and WorkOrdersDAOImplementation DAO's.
 */
public class ProfileWorkOrdersController {

    @FXML
    private ListView<WorkOrder> pendingWorkOrdersListView; //ListView containing pending work orders

    private final WorkOrdersDAOImplementation workOrdersDAO;

    public ProfileWorkOrdersController() throws SQLException {

        // Initialises a new UsersDAO and CustomerDAO interface (and connections)
        UsersDAO usersDAO = new UsersDAO();
        CustomerDAO customerDAO = new CustomerDAO();

        // Creates HashMaps that retrieve HashMaps containing all users and customers
        List<ProductionUser> productionUsers = usersDAO.getProductionUsers();
        List<Customer> customers = customerDAO.getAllCustomers();

        // Initialises WorkOrdersDAOImplementation using the recently initialised UsersDAO and CustomersDAO objects
        this.workOrdersDAO = new WorkOrdersDAOImplementation(productionUsers, customers);
    }

    /**
     * Initialises the controller after the FXML is loaded. The method
     * is automatically loaded by the FXMLLoader, displaying pending
     * work orders in the ListView.
     */
    @FXML
    private void initialize() {
        displayPendingWorkOrders(); // Displays pending work orders in the list view once controller initialises
    }

    /**
     * Retrieves and displays pending work orders in the ListView.
     * Retrieves work orders from the database and sets them in the ListView as ListCells.
     */
    @FXML
    private void displayPendingWorkOrders() {
        List<WorkOrder> pendingWorkOrders;
        try {
            // Retrieves pending work orders
            pendingWorkOrders = workOrdersDAO.getWorkOrderByStatus("pending");
            System.out.println("Fetched Work Orders: " + pendingWorkOrders);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Sets cell factory for work order display
        pendingWorkOrdersListView.setCellFactory(listView -> new ListCell<>() {

            @Override
            protected void updateItem(WorkOrder item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText("Work Order ID: " + item.getWorkOrderId() +
                            "\nOrder Date: " + item.getOrderDate().toString().substring(0,10));
                }
            }
        });

        // Updates ListView with retrieved pending work orders
        if (pendingWorkOrders != null) {
            pendingWorkOrdersListView.getItems().setAll(pendingWorkOrders);
        } else {
            pendingWorkOrdersListView.getItems().clear(); // Clear if null
        }
    }

    private void acceptWorkOrder() {

    }
}