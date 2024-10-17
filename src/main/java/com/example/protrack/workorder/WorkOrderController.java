package com.example.protrack.workorder;

import com.example.protrack.Main;
import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAO;
import com.example.protrack.customer.CustomerDAOImplementation;
import com.example.protrack.observers.Observer;
import com.example.protrack.observers.WorkOrderTableSubject;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.workorderproducts.WorkOrderProduct;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.*;
import javafx.util.Callback;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class WorkOrderController implements Observer {

    private static final String TITLE = "Create Work Order";
    private static final int WIDTH = 900;
    private static final int HEIGHT = 650;

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
    private TableColumn<WorkOrder, String> colStatus;
    @FXML
    private TableColumn<WorkOrder, Double> colSubtotal;
    @FXML
    private Button createWorkOrderButton;
    @FXML
    private Button exportToCsvButton;

    private ObservableList<WorkOrder> workOrders;
    private WorkOrderTableSubject workOrderSubject;

    /**
     * Initialises the controller class, automatically after the FXML file loads.
     */
    public void initialize() {
        workOrderSubject = new WorkOrderTableSubject();
        workOrderSubject.registerObserver(this);

        // Create a DateTimeFormatter for formatting the date
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Sets the TableView columns with the corresponding property values
        colWorkOrderId.setCellValueFactory(new PropertyValueFactory<>("workOrderId"));
        colOrderOwner.setCellValueFactory(new PropertyValueFactory<>("orderOwner"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customer"));

        // Custom cell factory for colOrderDate
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate")); // Just bind the property
        colOrderDate.setCellFactory(column -> new TableCell<WorkOrder, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(dateFormatter));
                }
            }
        });

        // Custom cell factory for colDeliveryDate
        colDeliveryDate.setCellValueFactory(new PropertyValueFactory<>("deliveryDate")); // Just bind the property
        colDeliveryDate.setCellFactory(column -> new TableCell<WorkOrder, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(dateFormatter));
                }
            }
        });

        colShippingAddress.setCellValueFactory(new PropertyValueFactory<>("shippingAddress"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        // Initialise the ObservableList and set it to the TableView
        workOrders = FXCollections.observableArrayList();
        workOrderTable.setItems(workOrders);

        // Handles row clicks to display relevant information
        workOrderTable.setRowFactory(tv -> {
            TableRow<WorkOrder> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    WorkOrder selectedWorkOrder = row.getItem();
                    editWorkOrderPopup(selectedWorkOrder);
                }
            });
            return row;
        });

        // Load and display the initial list of work orders
        workOrderSubject.syncDataFromDB();
        update();

        Platform.runLater(() -> {
            Window window = createWorkOrderButton.getScene().getWindow();
            if (window instanceof Stage stage) {
                stage.setOnCloseRequest(event -> workOrderSubject.deregisterObserver(this));
            }
        });
    }

    /**
     * Refreshes the TableView with the latest work orders from the database.
     */
    @Override
    public void update() {
        workOrders.clear();
        workOrders.setAll(workOrderSubject.getData());
    }

    /**
     * Opens a popup window to create a new work order.
     */
    public void createWorkOrderPopup() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/workorder/create_work_order.fxml"));
            Parent createWorkOrderRoot = fxmlLoader.load();

            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle(TITLE);

            Scene scene = new Scene(createWorkOrderRoot, WIDTH, HEIGHT);
            String stylesheet = Objects.requireNonNull(Main.class.getResource("stylesheet.css")).toExternalForm();
            scene.getStylesheets().add(stylesheet);
            popupStage.setScene(scene);

            // Show the popup window
            popupStage.showAndWait();
            workOrderSubject.syncDataFromDB();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editWorkOrderPopup(WorkOrder workOrder) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/workorder/edit_work_order.fxml"));
            Parent root = fxmlLoader.load();

            EditWorkOrderController controller = fxmlLoader.getController();
            controller.setWorkOrder(workOrder); // Pass the selected work order

            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Edit Work Order");

            Scene scene = new Scene(root);
            String stylesheet = Objects.requireNonNull(Main.class.getResource("stylesheet.css")).toExternalForm();
            scene.getStylesheets().add(stylesheet);
            popupStage.setScene(scene);

            popupStage.showAndWait();
            workOrderSubject.syncDataFromDB();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new work order to the database.
     *
     * @param orderOwner the user who owns the order
     * @param customer   the customer for whom the order is created
     * @param orderDate  the date the order was placed
     * @param productId  the ID of the product being ordered
     */
    public void addWorkOrder(ProductionUser orderOwner, Customer customer, LocalDateTime orderDate, Integer productId) {
        UsersDAO usersDAO = new UsersDAO();
        CustomerDAOImplementation customerDAOImplementation = new CustomerDAOImplementation();
        WorkOrdersDAOImplementation workOrdersDAO = new WorkOrdersDAOImplementation(
                usersDAO.getProductionUsers(),
                customerDAOImplementation.getAllCustomers()
        );

        WorkOrder newWorkOrder = new WorkOrder(
                1,  // ID can be generated by the database
                orderOwner,
                customer,
                orderDate,
                null,
                customer.getShippingAddress(),
                "Pending",
                21.00
        );

        workOrdersDAO.createWorkOrder(newWorkOrder);
        workOrderSubject.syncDataFromDB();
    }

    /**
     * Method that handles the export to CSV when the button is clicked.
     */
    public void exportToCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(exportToCsvButton.getScene().getWindow());

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                // Write the header row
                writer.write("Work Order ID,Owner,Customer,Order Date,Delivery Date,Shipping Address,Products,Status,Subtotal\n");

                // Fetch all work orders from the DAO
                List<ProductionUser> productionUsers = new UsersDAO().getProductionUsers();
                List<Customer> customers = new CustomerDAOImplementation().getAllCustomers();

                WorkOrdersDAOImplementation workOrdersDAO = new WorkOrdersDAOImplementation(productionUsers, customers);
                List<WorkOrder> workOrders = workOrdersDAO.getAllWorkOrders();

                // Loop through each work order and export details including products
                for (WorkOrder workOrder : workOrders) {
                    // Fetch the products for this work order
                    List<WorkOrderProduct> products = workOrder.getProducts();

                    // Create a formatted string of all product names and quantities
                    StringBuilder productDetails = new StringBuilder();
                    for (WorkOrderProduct product : products) {
                        productDetails.append(product.getProductName())
                                .append(" (Qty: ")
                                .append(product.getQuantity())
                                .append("), ");
                    }

                    // Remove the trailing comma and space if products exist
                    if (!productDetails.isEmpty()) {
                        productDetails.setLength(productDetails.length() - 2);
                    }

                    // Write the work order details to the CSV
                    writer.write(String.format("%d,%s,%s,%s,%s,\"%s\",\"%s\",%s,%.2f\n",
                            workOrder.getWorkOrderId(),
                            workOrder.getOrderOwner(),
                            workOrder.getCustomer(),
                            workOrder.getOrderDate(),
                            workOrder.getDeliveryDate(),
                            workOrder.getShippingAddress(),
                            productDetails,  // Include the products
                            workOrder.getStatus(),
                            workOrder.getSubtotal()
                    ));
                }

                System.out.println("CSV file created successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
