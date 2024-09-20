package com.example.protrack.workorder;

import com.example.protrack.Main;
import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAO;
import com.example.protrack.parts.Parts;
import com.example.protrack.parts.PartsDAO;
import com.example.protrack.products.ProductDAO;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class WorkOrderController {

    @FXML
    private TableView<WorkOrder> workOrderTable;

    @FXML
    private TableColumn<WorkOrder, Integer> colWorkOrderId;

    @FXML
    private TableColumn<WorkOrder, ProductionUser> colOrderOwner;

    @FXML
    private TableColumn<WorkOrder, Customer> colCustomer;

    @FXML
    private TableColumn<WorkOrder, LocalDateTime> colOrderDate;

    @FXML
    private TableColumn<WorkOrder, LocalDateTime> colDeliveryDate;

    @FXML
    private TableColumn<WorkOrder, String> colShippingAddress;

    @FXML
    private TableColumn<WorkOrder, Integer> colProducts;

    @FXML
    private TableColumn<WorkOrder, String> colStatus;

    @FXML
    private TableColumn<WorkOrder, Double> colSubtotal;

    @FXML
    private Button createWorkOrderButton;

    @FXML
    private Button refreshButton;

    private ObservableList<WorkOrder> workOrderList;

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     */
    public void initialize() {
        // Set up the TableView columns with the corresponding property values
        colWorkOrderId.setCellValueFactory(new PropertyValueFactory<>("workOrderId"));
        colOrderOwner.setCellValueFactory(new PropertyValueFactory<>("orderOwner"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customer"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colDeliveryDate.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));
        colShippingAddress.setCellValueFactory(new PropertyValueFactory<>("shippingAddress"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        // Initialize the ObservableList and set it to the TableView
        workOrderList = FXCollections.observableArrayList();
        workOrderTable.setItems(workOrderList);

        // Load and display the initial list of work orders
        refreshTable();
    }

    /**
     * Refreshes the TableView with the latest work orders from the database.
     */
    public void refreshTable() {
        // Create DAOs for fetching data
        UsersDAO usersDAO = new UsersDAO();
        CustomerDAO customerDAO = new CustomerDAO();
        WorkOrdersDAOImplementation workOrdersDAO = new WorkOrdersDAOImplementation(
                usersDAO.getProductionUsers(),
                customerDAO.getAllCustomers()
        );

        // Clear the current list and load the updated work orders
        workOrderList.clear();
        workOrderList.addAll(workOrdersDAO.getAllWorkOrders());
    }

    private static final String TITLE = "Create Work Order";
    private static final int WIDTH = 900;
    private static final int HEIGHT = 640;

    /**
     * Opens a popup window to create a new work order.
     */
    public void createWorkOrderPopup() {
        try {
            // Load the FXML file for the create work order dialog
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/workorder/create-work-order.fxml"));
            Parent createWorkOrderRoot = fxmlLoader.load();

            // Set up the popup stage
            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle(TITLE);

            // Create the scene and apply styles
            Scene scene = new Scene(createWorkOrderRoot, WIDTH, HEIGHT);
            String stylesheet = Objects.requireNonNull(Main.class.getResource("stylesheet.css")).toExternalForm();
            scene.getStylesheets().add(stylesheet);
            popupStage.setScene(scene);

            // Center the popup window on the screen
            Bounds rootBounds = createWorkOrderButton.getScene().getRoot().getLayoutBounds();
            popupStage.setY(rootBounds.getCenterY() - 260);
            popupStage.setX(rootBounds.getCenterX() - 310);

            // Show the popup window
            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new work order to the database.
     *
     * @param orderOwner the user who owns the order
     * @param customer the customer for whom the order is created
     * @param orderDate the date the order was placed
     * @param productId the ID of the product being ordered
     */
    public void addWorkOrder(ProductionUser orderOwner, Customer customer, LocalDateTime orderDate, Integer productId) {
        // Create DAOs for fetching data
        UsersDAO usersDAO = new UsersDAO();
        CustomerDAO customerDAO = new CustomerDAO();
        WorkOrdersDAOImplementation workOrdersDAO = new WorkOrdersDAOImplementation(
                usersDAO.getProductionUsers(),
                customerDAO.getAllCustomers()
        );

        // Create a new work order and add it to the database
        WorkOrder newWorkOrder = new WorkOrder(
                1,
                orderOwner,
                customer,
                orderDate,
                null,
                customer.getShippingAddress(),
                "Pending",
                21.00
        );
        workOrdersDAO.createWorkOrder(newWorkOrder);
    }
}