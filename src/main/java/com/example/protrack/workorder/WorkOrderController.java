package com.example.protrack.workorder;

import com.example.protrack.Main;
import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAOImplementation;
import com.example.protrack.observers.Observer;
import com.example.protrack.observers.WorkOrderTableSubject;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.IOException;
import java.time.LocalDateTime;
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
    private TableColumn<WorkOrder, Integer> colProducts;
    @FXML
    private TableColumn<WorkOrder, String> colStatus;
    @FXML
    private TableColumn<WorkOrder, Double> colSubtotal;
    @FXML
    private Button createWorkOrderButton;

    private ObservableList<WorkOrder> workOrders;
    private WorkOrderTableSubject workOrderSubject;

    /**
     * Initialises the controller class, automatically after the FXML file loads.
     */
    public void initialize() {
        workOrderSubject = new WorkOrderTableSubject();
        workOrderSubject.registerObserver(this);

        // Sets the TableView columns with the corresponding property values
        colWorkOrderId.setCellValueFactory(new PropertyValueFactory<>("workOrderId"));
        colOrderOwner.setCellValueFactory(new PropertyValueFactory<>("orderOwner"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customer"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colDeliveryDate.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));
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

            // Center the popup window on the screen
            centerPopupWindow(popupStage);

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

            // Center the popup window on the screen
            centerPopupWindow(popupStage);

            popupStage.showAndWait();
            workOrderSubject.syncDataFromDB();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Centers the popup window on the screen.
     */
    private void centerPopupWindow(Stage popupStage) {
        Bounds rootBounds = createWorkOrderButton.getScene().getRoot().getLayoutBounds();
        popupStage.setY(rootBounds.getCenterY() - (HEIGHT / 2));
        popupStage.setX(rootBounds.getCenterX() - (WIDTH / 2));
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
}
