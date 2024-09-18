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

    private ObservableList<WorkOrder> workOrderList;

    @FXML
    private Button createWorkOrderButton;

    @FXML
    private Button refreshButton;

    public void initialize() {
        colWorkOrderId.setCellValueFactory(new PropertyValueFactory<>("workOrderId"));
        colOrderOwner.setCellValueFactory(new PropertyValueFactory<>("orderOwner"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customer"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colDeliveryDate.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));
        colShippingAddress.setCellValueFactory(new PropertyValueFactory<>("shippingAddress"));
        colProducts.setCellValueFactory(new PropertyValueFactory<>("products"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        workOrderList = FXCollections.observableArrayList();
        workOrderTable.setItems(workOrderList);

        refreshTable();

    }

    public void refreshTable() {
            UsersDAO usersDAO = new UsersDAO();
            List<ProductionUser> productionUsers = usersDAO.getProductionUsers();

            CustomerDAO customerDAO = new CustomerDAO();
            List<Customer> customers = customerDAO.getAllCustomers();
            WorkOrdersDAOImplementation workOrdersDAO = new WorkOrdersDAOImplementation(productionUsers, customers);

            workOrderList.clear();
            workOrderList.addAll(workOrdersDAO.getAllWorkOrders());
    }

    private static final String TITLE = "Create Work Order";
    private static final int WIDTH = 900;
    private static final int HEIGHT = 640;

    public void createWorkOrderPopup() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/workorder/create-work-order.fxml"));
            Parent createWorkOrderRoot = fxmlLoader.load();

            Stage popupStage = new Stage();

            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle(TITLE);

            Scene scene = new Scene(createWorkOrderRoot, WIDTH, HEIGHT);
            String stylesheet = Objects.requireNonNull(Main.class.getResource("stylesheet.css")).toExternalForm();
            scene.getStylesheets().add(stylesheet);
            popupStage.setScene(scene);

            Bounds rootBounds = createWorkOrderButton.getScene().getRoot().getLayoutBounds();
            popupStage.setY(rootBounds.getCenterY() - 260);
            popupStage.setX(rootBounds.getCenterX() - 310);

            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addWorkOrder ( ProductionUser orderOwner, Customer customer, LocalDateTime orderDate, Integer productId) {
        UsersDAO usersDAO = new UsersDAO();
        List<ProductionUser> productionUsers = usersDAO.getProductionUsers();

        CustomerDAO customerDAO = new CustomerDAO();
        List<Customer> customers = customerDAO.getAllCustomers();

        WorkOrdersDAOImplementation workOrdersDAO = new WorkOrdersDAOImplementation(productionUsers, customers);
        WorkOrder newWorkOrder = new WorkOrder(1,
                orderOwner,
                customer,
                LocalDateTime.now(),
                null,
                customer.getShippingAddress(),
                1,
                "Pending",
                21.00);
        workOrdersDAO.createWorkOrder(newWorkOrder);
    }

}


