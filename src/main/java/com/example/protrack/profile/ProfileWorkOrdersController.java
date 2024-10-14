package com.example.protrack.profile;

import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAOImplementation;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.utility.LoggedInUserSingleton;
import com.example.protrack.workorder.WorkOrder;
import com.example.protrack.workorder.WorkOrdersDAOImplementation;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.sql.SQLException;
import java.util.List;

/**
 * Controller for managing and displaying work orders on the profile page.
 * The controller display of pending work orders with a List View, interacting with
 * User, Customer, and WorkOrdersDAOImplementation DAO's.
 */
public class ProfileWorkOrdersController {

    private final WorkOrdersDAOImplementation workOrdersDAO;
    @FXML
    private ListView<WorkOrder> ownedWorkOrdersListView; //ListView containing pending work orders

    public ProfileWorkOrdersController() {

        // Initialises a new UsersDAO and CustomerDAO interface (and connections)
        UsersDAO usersDAO = new UsersDAO();
        CustomerDAOImplementation customerDAOImplementation = new CustomerDAOImplementation();

        // Creates HashMaps that retrieve HashMaps containing all users and customers
        List<ProductionUser> productionUsers = usersDAO.getProductionUsers();
        List<Customer> customers = customerDAOImplementation.getAllCustomers();

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
        displayOwnedWorkOrders(); // Displays pending work orders in the list view once controller initialises
    }

    /**
     * Retrieves and displays pending work orders in the ListView.
     * Retrieves work orders from the database and sets them in the ListView as ListCells.
     */
    @FXML
    private void displayOwnedWorkOrders() {
        List<WorkOrder> ownedWorkOrders;

        // Retrieves pending work orders
        ownedWorkOrders = workOrdersDAO.getWorkOrdersByEmployeeId(LoggedInUserSingleton.getInstance().getEmployeeId());
        System.out.println("Fetched Work Orders: " + ownedWorkOrders);


        // Sets cell factory for work order display
        ownedWorkOrdersListView.setCellFactory(listView -> new ListCell<>() {

            @Override
            protected void updateItem(WorkOrder item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText("Work Order ID: " + item.getWorkOrderId() +
                            "\nOrder Date: " + item.getOrderDate().toString().substring(0, 10));
                }
            }
        });

        // Updates ListView with retrieved pending work orders
        if (ownedWorkOrders != null) {
            ownedWorkOrdersListView.getItems().setAll(ownedWorkOrders);
        } else {
            ownedWorkOrdersListView.getItems().clear(); // Clear if null
        }
    }

    private void acceptWorkOrder() {

    }
}